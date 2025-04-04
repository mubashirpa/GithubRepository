package com.evaluation.githubrepository.data.remote.dto.github.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class License(
    @SerialName("html_url")
    val htmlUrl: String? = null,
    val key: String? = null,
    val name: String? = null,
    @SerialName("node_id")
    val nodeId: String? = null,
    @SerialName("spdx_id")
    val spdxId: String? = null,
    val url: String? = null,
)
