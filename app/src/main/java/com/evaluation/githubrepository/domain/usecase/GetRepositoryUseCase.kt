package com.evaluation.githubrepository.domain.usecase

import com.evaluation.githubrepository.R
import com.evaluation.githubrepository.core.Result
import com.evaluation.githubrepository.core.UiText
import com.evaluation.githubrepository.data.mapper.toRepoDetails
import com.evaluation.githubrepository.domain.model.github.repo.RepoDetails
import com.evaluation.githubrepository.domain.repository.GitHubRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.net.ConnectException

class GetRepositoryUseCase(
    private val repository: GitHubRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    operator fun invoke(
        owner: String,
        repo: String,
    ): Flow<Result<RepoDetails>> =
        flow {
            try {
                emit(Result.Loading())
                val local = repository.getRepository(owner, repo)
                if (local != null) {
                    emit(Result.Success(local.toRepoDetails()))
                } else {
                    val token = "" // TODO
                    repository.getRepository(token, owner, repo)?.toRepoDetails()?.let {
                        emit(Result.Success(it))
                    } ?: run {
                        emit(Result.Error(UiText.StringResource(R.string.error_unknown)))
                    }
                }
            } catch (e: ConnectException) {
                e.printStackTrace()
                emit(Result.Error(UiText.StringResource(R.string.error_connect)))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Result.Error(UiText.StringResource(R.string.error_unknown)))
            }
        }.flowOn(ioDispatcher)
}
