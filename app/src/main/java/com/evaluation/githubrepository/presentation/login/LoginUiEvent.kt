package com.evaluation.githubrepository.presentation.login

sealed class LoginUiEvent {
    data class Login(
        val token: String,
        val nonce: String,
    ) : LoginUiEvent()

    data object UserMessageShown : LoginUiEvent()
}
