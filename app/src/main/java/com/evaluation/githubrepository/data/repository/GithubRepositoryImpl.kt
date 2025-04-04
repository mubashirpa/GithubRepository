package com.evaluation.githubrepository.data.repository

import com.evaluation.githubrepository.core.Constants
import com.evaluation.githubrepository.data.remote.dto.github.repos.RepoDto
import com.evaluation.githubrepository.data.remote.dto.github.search.SearchRepoDto
import com.evaluation.githubrepository.domain.repository.GitHubRepository
import com.evaluation.githubrepository.domain.repository.RepoDirection
import com.evaluation.githubrepository.domain.repository.RepoSort
import com.evaluation.githubrepository.domain.repository.RepoType
import com.evaluation.githubrepository.domain.repository.SearchOrder
import com.evaluation.githubrepository.domain.repository.SearchSort
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpHeaders
import io.ktor.http.appendPathSegments
import io.ktor.http.headers

class GithubRepositoryImpl(
    private val httpClient: HttpClient,
) : GitHubRepository {
    override suspend fun getRepositories(
        token: String,
        username: String,
        type: RepoType,
        sort: RepoSort,
        direction: RepoDirection,
        perPage: Int,
        page: Int,
    ): List<RepoDto> =
        httpClient
            .get(Constants.GITHUB_API_BASE_URL) {
                url {
                    appendPathSegments("users", username, "repos")
                    parameters.apply {
                        append("type", type.name.lowercase())
                        append("sort", sort.name.lowercase())
                        append("direction", direction.name.lowercase())
                        append("per_page", perPage.toString())
                        append("page", page.toString())
                    }
                }
                headers {
                    append(HttpHeaders.Accept, "application/vnd.github+json")
                    append(HttpHeaders.Authorization, "Bearer $token")
                    append("X-GitHub-Api-Version", "2022-11-28")
                }
            }.body()

    override suspend fun searchRepositories(
        token: String,
        query: String,
        sort: SearchSort?,
        order: SearchOrder,
        perPage: Int,
        page: Int,
    ): SearchRepoDto =
        httpClient
            .get(Constants.GITHUB_API_BASE_URL) {
                url {
                    appendPathSegments("search", "repositories")
                    parameters.apply {
                        append("q", query)
                        sort?.let { append("sort", sort.name.lowercase()) }
                        append("order", order.name.lowercase())
                        append("per_page", perPage.toString())
                        append("page", page.toString())
                    }
                }
                headers {
                    append(HttpHeaders.Accept, "application/vnd.github+json")
                    append(HttpHeaders.Authorization, "Bearer $token")
                    append("X-GitHub-Api-Version", "2022-11-28")
                }
            }.body()
}
