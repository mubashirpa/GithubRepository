package com.evaluation.githubrepository.domain.repository

import androidx.annotation.IntRange
import com.evaluation.githubrepository.data.remote.dto.github.repos.RepoDto

interface GitHubRepository {
    suspend fun getRepositories(
        token: String,
        username: String,
        type: RepoType = RepoType.OWNER,
        sort: RepoSort = RepoSort.FULL_NAME,
        direction: RepoDirection = if (sort == RepoSort.FULL_NAME) RepoDirection.ASC else RepoDirection.DESC,
        @IntRange(from = 1, to = 100) perPage: Int = 30,
        page: Int = 1,
    ): List<RepoDto>
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
