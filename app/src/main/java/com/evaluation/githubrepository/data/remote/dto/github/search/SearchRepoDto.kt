package com.evaluation.githubrepository.data.remote.dto.github.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchRepoDto(
    @SerialName("incomplete_results")
    val incompleteResults: Boolean? = null,
    val items: List<Item>? = null,
    @SerialName("total_count")
    val totalCount: Int? = null,
)
