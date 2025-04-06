package com.evaluation.githubrepository.presentation.repo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.evaluation.githubrepository.domain.usecase.GetRepositoryUseCase
import com.evaluation.githubrepository.navigation.Screen
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class RepoViewModel(
    savedStateHandle: SavedStateHandle,
    private val getRepositoryUseCase: GetRepositoryUseCase,
) : ViewModel() {
    var uiState by mutableStateOf(RepoUiState())
        private set

    private val repo = savedStateHandle.toRoute<Screen.Repo>()

    init {
        getRepository(repo.owner, repo.repo)
    }

    fun onEvent(event: RepoUiEvent) {
        when (event) {
            RepoUiEvent.Retry -> {
                getRepository(repo.owner, repo.repo)
            }
        }
    }

    private fun getRepository(
        owner: String,
        repo: String,
    ) {
        getRepositoryUseCase(owner, repo)
            .onEach { result ->
                uiState = uiState.copy(repoResult = result)
            }.launchIn(viewModelScope)
    }
}
