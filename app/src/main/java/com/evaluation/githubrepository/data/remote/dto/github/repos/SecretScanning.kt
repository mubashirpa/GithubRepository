package com.evaluation.githubrepository.data.remote.dto.github.repos

import kotlinx.serialization.Serializable

@Serializable
data class SecretScanning(
    val status: String? = null,
)
