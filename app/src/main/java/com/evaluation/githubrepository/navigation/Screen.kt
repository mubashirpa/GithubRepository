package com.evaluation.githubrepository.navigation

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object Login : Screen()

    @Serializable
    data object Home : Screen()

    @Serializable
    data object Settings : Screen()

    @Serializable
    data class Repo(
        val owner: String,
        val repo: String,
    ) : Screen()
}
