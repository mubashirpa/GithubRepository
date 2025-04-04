package com.evaluation.githubrepository.data.remote.dto.github.repos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SecurityAndAnalysis(
    @SerialName("advanced_security")
    val advancedSecurity: AdvancedSecurity? = null,
    @SerialName("secret_scanning")
    val secretScanning: SecretScanning? = null,
    @SerialName("secret_scanning_non_provider_patterns")
    val secretScanningNonProviderPatterns: SecretScanningNonProviderPatterns? = null,
    @SerialName("secret_scanning_push_protection")
    val secretScanningPushProtection: SecretScanningPushProtection? = null,
)
