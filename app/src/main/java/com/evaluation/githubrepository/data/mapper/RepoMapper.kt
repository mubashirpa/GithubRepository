package com.evaluation.githubrepository.data.mapper

import com.evaluation.githubrepository.data.remote.dto.github.repos.RepoDto
import com.evaluation.githubrepository.domain.model.github.repos.Repo

fun RepoDto.toRepo(): Repo =
    Repo(
        description = description,
        id = id,
        name = name,
        stargazersCount = stargazersCount,
    )
