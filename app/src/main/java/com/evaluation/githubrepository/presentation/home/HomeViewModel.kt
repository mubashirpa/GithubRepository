package com.evaluation.githubrepository.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evaluation.githubrepository.domain.usecase.GetRepositoriesUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class HomeViewModel(
    private val getRepositoriesUseCase: GetRepositoriesUseCase,
) : ViewModel() {
    var uiState by mutableStateOf(HomeUiState())
        private set

    init {
        getRepositories()
    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            HomeUiEvent.Retry -> {
                getRepositories()
            }
        }
    }

    private fun getRepositories() {
        getRepositoriesUseCase("google")
            .onEach { result ->
                uiState = uiState.copy(repositoriesResult = result)
            }.launchIn(viewModelScope)
    }
}
