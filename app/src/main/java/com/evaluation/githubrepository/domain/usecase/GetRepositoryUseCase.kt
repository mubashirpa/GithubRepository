package com.evaluation.githubrepository.domain.usecase

import com.evaluation.githubrepository.R
import com.evaluation.githubrepository.core.Result
import com.evaluation.githubrepository.core.UiText
import com.evaluation.githubrepository.data.mapper.toRepoDetails
import com.evaluation.githubrepository.data.mapper.toRepoDetailsEntity
import com.evaluation.githubrepository.domain.model.github.repo.RepoDetails
import com.evaluation.githubrepository.domain.repository.GitHubRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.net.ConnectException
import java.util.concurrent.TimeUnit

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
                // Check for repo locally
                val localRepo = withContext(ioDispatcher) { repository.getRepository(owner, repo) }

                // Check if repo is cached and not expired
                if (localRepo != null && isCacheValid(localRepo.lastFetchedAt)) {
                    // Emit local repo
                    emit(Result.Success(localRepo.toRepoDetails()))
                } else {
                    // Fetch remote repo
                    emitFromRemote(owner, repo)
                }
            } catch (_: ConnectException) {
                emit(Result.Error(UiText.StringResource(R.string.error_connect)))
            } catch (_: Exception) {
                emit(Result.Error(UiText.StringResource(R.string.error_unknown)))
            }
        }.flowOn(ioDispatcher)

    private suspend fun FlowCollector<Result<RepoDetails>>.emitFromRemote(
        owner: String,
        repo: String,
    ) {
        val token = "" // TODO
        val repo = withContext(ioDispatcher) { repository.getRepository(token, owner, repo) }

        repo?.let {
            val lastFetchedAt = System.currentTimeMillis()
            // Insert repo into database
            withContext(ioDispatcher) {
                repository.insertRepository(it.toRepoDetailsEntity(lastFetchedAt))
            }
            // Emit remote repo
            emit(Result.Success(it.toRepoDetails()))
        } ?: run {
            emit(Result.Error(UiText.StringResource(R.string.error_unknown)))
        }
    }

    private fun isCacheValid(lastFetchedAt: Long): Boolean {
        // Check if cache is valid (1 hour)
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)
        return System.currentTimeMillis() - lastFetchedAt <= cacheTimeout
    }
}
