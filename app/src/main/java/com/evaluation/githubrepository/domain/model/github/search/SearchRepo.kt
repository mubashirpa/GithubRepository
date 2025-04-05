package com.evaluation.githubrepository.domain.model.github.search

data class SearchRepo(
    val description: String? = null,
    val id: Int? = null,
    val name: String? = null,
    val owner: String? = null,
    val stargazersCount: Int? = null,
)
