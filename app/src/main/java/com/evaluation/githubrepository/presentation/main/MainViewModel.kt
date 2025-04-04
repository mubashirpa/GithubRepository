package com.evaluation.githubrepository.presentation.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.evaluation.githubrepository.domain.usecase.IsUserLoggedInUseCase

class MainViewModel(
    isUserLoggedInUseCase: IsUserLoggedInUseCase,
) : ViewModel() {
    var uiState by mutableStateOf(MainUiState())
        private set

    init {
        isUserLoggedInUseCase().let { isUserLoggedIn ->
            uiState =
                uiState.copy(
                    isLoading = false,
                    isUserLoggedIn = isUserLoggedIn,
                )
        }
    }
}
