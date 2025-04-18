package com.evaluation.githubrepository.data.remote.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.evaluation.githubrepository.data.local.database.GithubDatabase
import com.evaluation.githubrepository.data.local.entity.CacheMetadataEntity
import com.evaluation.githubrepository.data.local.entity.RemoteKeysEntity
import com.evaluation.githubrepository.data.local.entity.ReposEntity
import com.evaluation.githubrepository.data.mapper.toRepoEntity
import com.evaluation.githubrepository.domain.repository.GithubRepository
import com.evaluation.githubrepository.domain.repository.RepoDirection
import com.evaluation.githubrepository.domain.repository.RepoSort
import com.evaluation.githubrepository.domain.repository.RepoType
import java.io.IOException
import java.lang.Exception
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class RepoRemoteMediator(
    private val database: GithubDatabase,
    private val gitHubRepository: GithubRepository,
    private val token: String,
    private val username: String,
    private val type: RepoType = RepoType.OWNER,
    private val sort: RepoSort = RepoSort.FULL_NAME,
    private val direction: RepoDirection = if (sort == RepoSort.FULL_NAME) RepoDirection.ASC else RepoDirection.DESC,
    private val page: Int = 1,
) : RemoteMediator<Int, ReposEntity>() {
    val remoteKeysDao = database.remoteKeysDao()
    val reposDao = database.reposDao()
    val cacheMetadataDao = database.cacheMetadataDao()

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)
        val lastUpdated = database.lastUpdated()
        return if (System.currentTimeMillis() - lastUpdated <= cacheTimeout) {
            // Cached data is fresh (within 1 hour), skip refresh
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            // Cached data is stale or missing, trigger refresh
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ReposEntity>,
    ): MediatorResult {
        return try {
            val perPage = state.config.pageSize
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
                    perPage = perPage,
                    page = loadKey,
                )
            val repositories =
                response.mapIndexed { index, repo ->
                    repo.toRepoEntity(
                        displayOrder = (loadKey - 1) * perPage + index + 1,
                    )
                }
            val endOfPaginationReached = repositories.size < perPage

            database.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    remoteKeysDao.clearRemoteKeys()
                    reposDao.clearAll()
                    cacheMetadataDao.clear()
                }

                val prevKey = if (loadKey == page) null else loadKey - 1
                val nextKey = if (endOfPaginationReached) null else loadKey + 1

                val keys =
                    repositories.map {
                        RemoteKeysEntity(id = it.id, prevKey = prevKey, nextKey = nextKey)
                    }
                remoteKeysDao.insertAll(keys)
                reposDao.insertAll(repositories)
                cacheMetadataDao.insert(
                    CacheMetadataEntity(
                        id = 1,
                        lastUpdated = System.currentTimeMillis(),
                    ),
                )
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
