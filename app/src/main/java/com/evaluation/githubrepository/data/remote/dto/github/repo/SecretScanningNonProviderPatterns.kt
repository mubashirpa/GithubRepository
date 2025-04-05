package com.evaluation.githubrepository.data.remote.dto.github.repo

import kotlinx.serialization.Serializable

@Serializable
data class SecretScanningNonProviderPatterns(
    val status: String? = null,
)
