package com.evaluation.githubrepository.domain.usecase

import androidx.annotation.IntRange
import com.evaluation.githubrepository.R
import com.evaluation.githubrepository.core.Result
import com.evaluation.githubrepository.core.UiText
import com.evaluation.githubrepository.data.mapper.toSearchRepo
import com.evaluation.githubrepository.domain.model.github.search.SearchRepo
import com.evaluation.githubrepository.domain.repository.GitHubRepository
import com.evaluation.githubrepository.domain.repository.SearchOrder
import com.evaluation.githubrepository.domain.repository.SearchSort
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.net.ConnectException

class SearchRepositoriesUseCase(
    private val repository: GitHubRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    operator fun invoke(
        query: String,
        sort: SearchSort? = null,
        order: SearchOrder = SearchOrder.DESC,
        @IntRange(from = 1, to = 100) perPage: Int = 30,
        page: Int = 1,
    ): Flow<Result<List<SearchRepo>>> =
        flow {
            try {
                emit(Result.Loading())
                val token = "" // TODO
                val repositories =
                    repository
                        .searchRepositories(
                            token = token,
                            query = query,
                            sort = sort,
                            order = order,
                            perPage = perPage,
                            page = page,
                        ).toSearchRepo()
                emit(Result.Success(repositories))
            } catch (_: ConnectException) {
                emit(Result.Error(UiText.StringResource(R.string.error_connect)))
            } catch (_: Exception) {
                emit(Result.Error(UiText.StringResource(R.string.error_unknown)))
            }
        }.flowOn(ioDispatcher)
}
