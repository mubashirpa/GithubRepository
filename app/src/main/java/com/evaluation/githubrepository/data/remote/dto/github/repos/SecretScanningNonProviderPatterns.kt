package com.evaluation.githubrepository.data.remote.dto.github.repos

import kotlinx.serialization.Serializable

@Serializable
data class SecretScanningNonProviderPatterns(
    val status: String? = null,
)
