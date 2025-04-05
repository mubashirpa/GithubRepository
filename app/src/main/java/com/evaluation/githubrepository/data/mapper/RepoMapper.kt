package com.evaluation.githubrepository.data.mapper

import com.evaluation.githubrepository.data.local.entity.ReposEntity
import com.evaluation.githubrepository.data.remote.dto.github.repos.RepoDto
import com.evaluation.githubrepository.data.remote.dto.github.search.SearchRepoDto
import com.evaluation.githubrepository.domain.model.github.repos.Repo
import com.evaluation.githubrepository.domain.model.github.search.SearchRepo

fun RepoDto.toRepo(): Repo =
    Repo(
        description = description,
        id = id,
        name = name,
        stargazersCount = stargazersCount,
    )

fun RepoDto.toRepoEntity(): ReposEntity =
    ReposEntity(
        id = id!!,
        description = description,
        name = name,
        stargazersCount = stargazersCount,
    )

fun ReposEntity.toRepo(): Repo =
    Repo(
        description = description,
        id = id,
        name = name,
        stargazersCount = stargazersCount,
    )

fun SearchRepoDto.toSearchRepo(): List<SearchRepo> =
    items
        ?.map { repo ->
            SearchRepo(
                description = repo.description,
                id = repo.id,
                name = repo.name,
                stargazersCount = repo.stargazersCount,
            )
        }.orEmpty()
