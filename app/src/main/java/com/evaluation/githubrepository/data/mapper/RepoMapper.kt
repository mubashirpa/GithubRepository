package com.evaluation.githubrepository.data.mapper

import com.evaluation.githubrepository.data.local.entity.RepoDetailsEntity
import com.evaluation.githubrepository.data.local.entity.ReposEntity
import com.evaluation.githubrepository.data.remote.dto.github.repo.RepoDetailsDto
import com.evaluation.githubrepository.data.remote.dto.github.repos.RepoDto
import com.evaluation.githubrepository.data.remote.dto.github.search.SearchRepoDto
import com.evaluation.githubrepository.domain.model.github.repo.RepoDetails
import com.evaluation.githubrepository.domain.model.github.repos.Repo
import com.evaluation.githubrepository.domain.model.github.search.SearchRepo

// Repo List

fun RepoDto.toRepo(): Repo =
    Repo(
        description = description,
        id = id,
        language = language,
        name = name,
        owner = owner?.login,
        stargazersCount = stargazersCount,
    )

fun RepoDto.toRepoEntity(displayOrder: Int): ReposEntity =
    ReposEntity(
        id = id!!,
        displayOrder = displayOrder,
        description = description,
        language = language,
        name = name,
        owner = owner?.login,
        stargazersCount = stargazersCount,
    )

fun ReposEntity.toRepo(): Repo =
    Repo(
        description = description,
        id = id,
        language = language,
        name = name,
        owner = owner,
        stargazersCount = stargazersCount,
    )

// Search Repo

fun SearchRepoDto.toSearchRepo(): List<SearchRepo> =
    items
        ?.map { repo ->
            SearchRepo(
                description = repo.description,
                id = repo.id,
                language = repo.language,
                name = repo.name,
                owner = repo.owner?.login,
                stargazersCount = repo.stargazersCount,
            )
        }.orEmpty()

// Repo Details

fun RepoDetailsDto.toRepoDetails(): RepoDetails =
    RepoDetails(
        description = description,
        id = id,
        language = language,
        name = name,
        owner = owner?.login,
        stargazersCount = stargazersCount,
    )

fun RepoDetailsDto.toRepoDetailsEntity(lastFetchedAt: Long): RepoDetailsEntity =
    RepoDetailsEntity(
        id = id!!,
        lastFetchedAt = lastFetchedAt,
        description = description,
        language = language,
        name = name,
        owner = owner?.login,
        stargazersCount = stargazersCount,
    )

fun RepoDetailsEntity.toRepoDetails(): RepoDetails =
    RepoDetails(
        description = description,
        id = id,
        language = language,
        name = name,
        owner = owner,
        stargazersCount = stargazersCount,
    )
