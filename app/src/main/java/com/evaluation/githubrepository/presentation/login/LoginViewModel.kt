package com.evaluation.githubrepository.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evaluation.githubrepository.core.Result
import com.evaluation.githubrepository.domain.usecase.LoginWithGoogleUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class LoginViewModel(
    private val loginWithGoogleUseCase: LoginWithGoogleUseCase,
) : ViewModel() {
    var uiState by mutableStateOf(LoginUiState())
        private set

    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.Login -> {
                login(event.token, event.nonce)
            }

            LoginUiEvent.UserMessageShown -> {
                uiState = uiState.copy(userMessage = null)
            }
        }
    }

    private fun login(
        token: String,
        nonce: String,
    ) {
        loginWithGoogleUseCase(token, nonce)
            .onEach { result ->
                uiState =
                    when (result) {
                        is Result.Empty -> {
                            uiState
                        }

                        is Result.Error -> {
                            uiState.copy(
                                openProgressDialog = false,
                                userMessage = result.message,
                            )
                        }

                        is Result.Loading -> {
                            uiState.copy(openProgressDialog = true)
                        }

                        is Result.Success -> {
                            uiState.copy(
                                isUserLoggedIn = true,
                                openProgressDialog = false,
                            )
                        }
                    }
            }.launchIn(viewModelScope)
    }
}
