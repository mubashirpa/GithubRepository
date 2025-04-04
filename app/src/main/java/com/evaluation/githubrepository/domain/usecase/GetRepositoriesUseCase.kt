package com.evaluation.githubrepository.domain.usecase

import androidx.annotation.IntRange
import com.evaluation.githubrepository.R
import com.evaluation.githubrepository.core.Result
import com.evaluation.githubrepository.core.UiText
import com.evaluation.githubrepository.data.mapper.toRepo
import com.evaluation.githubrepository.domain.model.github.repos.Repo
import com.evaluation.githubrepository.domain.repository.GitHubRepository
import com.evaluation.githubrepository.domain.repository.RepoDirection
import com.evaluation.githubrepository.domain.repository.RepoSort
import com.evaluation.githubrepository.domain.repository.RepoType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.net.ConnectException

class GetRepositoriesUseCase(
    private val repository: GitHubRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    operator fun invoke(
        username: String,
        type: RepoType = RepoType.OWNER,
        sort: RepoSort = RepoSort.FULL_NAME,
        direction: RepoDirection = if (sort == RepoSort.FULL_NAME) RepoDirection.ASC else RepoDirection.DESC,
        @IntRange(from = 1, to = 100) perPage: Int = 30,
        page: Int = 1,
    ): Flow<Result<List<Repo>>> =
        flow {
            try {
                emit(Result.Loading())
                val token = "" // TODO
                val repositories =
                    repository
                        .getRepositories(
                            token = token,
                            username = username,
                            type = type,
                            sort = sort,
                            direction = direction,
                            perPage = perPage,
                            page = page,
                        ).map { it.toRepo() }
                emit(Result.Success(repositories))
            } catch (_: ConnectException) {
                emit(Result.Error(UiText.StringResource(R.string.error_connect)))
            } catch (_: Exception) {
                emit(Result.Error(UiText.StringResource(R.string.error_unknown)))
            }
        }.flowOn(ioDispatcher)
}
