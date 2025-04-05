package com.evaluation.githubrepository.data.remote.dto.github.repo

import kotlinx.serialization.Serializable

@Serializable
data class SecretScanning(
    val status: String? = null,
)
