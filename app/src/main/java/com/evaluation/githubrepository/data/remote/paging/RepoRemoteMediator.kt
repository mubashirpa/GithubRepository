package com.evaluation.githubrepository.data.remote.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.evaluation.githubrepository.data.local.database.GithubDatabase
import com.evaluation.githubrepository.data.local.entity.RemoteKeysEntity
import com.evaluation.githubrepository.data.local.entity.ReposEntity
import com.evaluation.githubrepository.data.mapper.toRepoEntity
import com.evaluation.githubrepository.domain.repository.GitHubRepository
import com.evaluation.githubrepository.domain.repository.RepoDirection
import com.evaluation.githubrepository.domain.repository.RepoSort
import com.evaluation.githubrepository.domain.repository.RepoType
import kotlinx.io.IOException
import java.lang.Exception

@OptIn(ExperimentalPagingApi::class)
class RepoRemoteMediator(
    private val database: GithubDatabase,
    private val gitHubRepository: GitHubRepository,
    private val token: String,
    private val username: String,
    private val type: RepoType = RepoType.OWNER,
    private val sort: RepoSort = RepoSort.FULL_NAME,
    private val direction: RepoDirection = if (sort == RepoSort.FULL_NAME) RepoDirection.ASC else RepoDirection.DESC,
    private val page: Int = 1,
) : RemoteMediator<Int, ReposEntity>() {
    val remoteKeysDao = database.remoteKeysDao()
    val reposDao = database.reposDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ReposEntity>,
    ): MediatorResult {
        return try {
            val loadKey =
                when (loadType) {
                    LoadType.REFRESH -> {
                        val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                        remoteKeys?.nextKey?.minus(1) ?: page
                    }

                    LoadType.PREPEND -> {
                        val remoteKeys = getRemoteKeyForFirstItem(state)
                        val prevKey = remoteKeys?.prevKey
                        if (prevKey == null) {
                            return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                        }
                        prevKey
                    }

                    LoadType.APPEND -> {
                        val remoteKeys = getRemoteKeyForLastItem(state)
                        val nextKey = remoteKeys?.nextKey
                        if (nextKey == null) {
                            return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                        }
                        nextKey
                    }
                }

            val response =
                gitHubRepository.getRepositories(
                    token = token,
                    username = username,
                    type = type,
                    sort = sort,
                    direction = direction,
                    perPage = state.config.pageSize,
                    page = loadKey,
                )
            val repositories = response.map { it.toRepoEntity() }
            val endOfPaginationReached = repositories.size < state.config.pageSize

            database.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    remoteKeysDao.clearRemoteKeys()
                    reposDao.clearAll()
                }

                val prevKey = if (loadKey == page) null else loadKey - 1
                val nextKey = if (endOfPaginationReached) null else loadKey + 1

                val keys =
                    repositories.map {
                        RemoteKeysEntity(id = it.id, prevKey = prevKey, nextKey = nextKey)
                    }
                remoteKeysDao.insertAll(keys)
                reposDao.insertAll(repositories)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, ReposEntity>): RemoteKeysEntity? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data
            ?.lastOrNull()
            ?.let { repo ->
                // Get the remote keys of the last item retrieved
                remoteKeysDao.remoteKeysId(repo.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, ReposEntity>): RemoteKeysEntity? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data
            ?.firstOrNull()
            ?.let { repo ->
                // Get the remote keys of the first items retrieved
                remoteKeysDao.remoteKeysId(repo.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, ReposEntity>): RemoteKeysEntity? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                remoteKeysDao.remoteKeysId(repoId)
            }
        }
    }
}
