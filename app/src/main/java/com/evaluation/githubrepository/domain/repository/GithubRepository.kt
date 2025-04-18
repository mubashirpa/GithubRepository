package com.evaluation.githubrepository.domain.repository

import androidx.annotation.IntRange
import androidx.paging.PagingData
import com.evaluation.githubrepository.data.local.entity.RepoDetailsEntity
import com.evaluation.githubrepository.data.local.entity.ReposEntity
import com.evaluation.githubrepository.data.remote.dto.github.repo.RepoDetailsDto
import com.evaluation.githubrepository.data.remote.dto.github.repos.RepoDto
import com.evaluation.githubrepository.data.remote.dto.github.search.SearchRepoDto
import kotlinx.coroutines.flow.Flow

interface GithubRepository {
    suspend fun getRepositories(
        token: String,
        username: String,
        type: RepoType = RepoType.OWNER,
        sort: RepoSort = RepoSort.FULL_NAME,
        direction: RepoDirection = if (sort == RepoSort.FULL_NAME) RepoDirection.ASC else RepoDirection.DESC,
        @IntRange(from = 1, to = 100) perPage: Int = 30,
        page: Int = 1,
    ): List<RepoDto>

    suspend fun getRepositoriesPaging(
        token: String,
        username: String,
        type: RepoType = RepoType.OWNER,
        sort: RepoSort = RepoSort.FULL_NAME,
        direction: RepoDirection = if (sort == RepoSort.FULL_NAME) RepoDirection.ASC else RepoDirection.DESC,
        @IntRange(from = 1, to = 100) perPage: Int = 30,
        page: Int = 1,
    ): Flow<PagingData<ReposEntity>>

    suspend fun searchRepositories(
        token: String,
        query: String,
        sort: SearchSort? = null,
        order: SearchOrder = SearchOrder.DESC,
        @IntRange(from = 1, to = 100) perPage: Int = 30,
        page: Int = 1,
    ): SearchRepoDto

    suspend fun getRepository(
        owner: String,
        repo: String,
    ): RepoDetailsEntity?

    suspend fun insertRepository(repo: RepoDetailsEntity)

    suspend fun getRepository(
        token: String,
        owner: String,
        repo: String,
    ): RepoDetailsDto?
}

enum class RepoType {
    ALL,
    OWNER,
    MEMBER,
}

enum class RepoSort {
    CREATED,
    UPDATED,
    PUSHED,
    FULL_NAME,
}

enum class RepoDirection {
    ASC,
    DESC,
}

enum class SearchSort {
    STARS,
    FORKS,
    HELP_WANTED_ISSUES,
    UPDATED,
}

enum class SearchOrder {
    ASC,
    DESC,
}
