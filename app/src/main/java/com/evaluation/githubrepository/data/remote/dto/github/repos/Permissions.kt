package com.evaluation.githubrepository.data.remote.dto.github.repos

import kotlinx.serialization.Serializable

@Serializable
data class Permissions(
    val admin: Boolean? = null,
    val pull: Boolean? = null,
    val push: Boolean? = null,
)
