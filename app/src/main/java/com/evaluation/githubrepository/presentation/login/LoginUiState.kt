package com.evaluation.githubrepository.presentation.login

import com.evaluation.githubrepository.core.UiText

data class LoginUiState(
    val isUserLoggedIn: Boolean = false,
    val openProgressDialog: Boolean = false,
    val userMessage: UiText? = null,
)
