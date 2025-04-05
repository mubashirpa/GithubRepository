package com.evaluation.githubrepository.domain.usecase

import androidx.annotation.IntRange
import androidx.paging.PagingData
import androidx.paging.map
import com.evaluation.githubrepository.data.mapper.toRepo
import com.evaluation.githubrepository.domain.model.github.repos.Repo
import com.evaluation.githubrepository.domain.repository.GitHubRepository
import com.evaluation.githubrepository.domain.repository.RepoDirection
import com.evaluation.githubrepository.domain.repository.RepoSort
import com.evaluation.githubrepository.domain.repository.RepoType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class GetRepositoriesPagingUseCase(
    private val repository: GitHubRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    suspend operator fun invoke(
        username: String,
        type: RepoType = RepoType.OWNER,
        sort: RepoSort = RepoSort.FULL_NAME,
        direction: RepoDirection = if (sort == RepoSort.FULL_NAME) RepoDirection.ASC else RepoDirection.DESC,
        @IntRange(from = 1, to = 100) perPage: Int = 30,
        page: Int = 1,
    ): Flow<PagingData<Repo>> =
        withContext(ioDispatcher) {
            val token = "" // TODO
            repository
                .getRepositoriesPaging(
                    token = token,
                    username = username,
                    type = type,
                    sort = sort,
                    direction = direction,
                    perPage = perPage,
                    page = page,
                ).map { it.map { it.toRepo() } }
        }
}
