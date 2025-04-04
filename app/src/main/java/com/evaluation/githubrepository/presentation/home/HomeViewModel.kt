package com.evaluation.githubrepository.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evaluation.githubrepository.core.Result
import com.evaluation.githubrepository.domain.usecase.GetRepositoriesUseCase
import com.evaluation.githubrepository.domain.usecase.SearchRepositoriesUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getRepositoriesUseCase: GetRepositoriesUseCase,
    private val searchRepositoriesUseCase: SearchRepositoriesUseCase,
) : ViewModel() {
    var uiState by mutableStateOf(HomeUiState())
        private set

    private var searchJob: Job? = null

    init {
        getRepositories()

        viewModelScope.launch {
            snapshotFlow { uiState.searchFieldState.text }.collectLatest { queryText ->
                searchRepositories(queryText.toString(), 500)
            }
        }
    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            HomeUiEvent.Retry -> {
                getRepositories()
            }

            is HomeUiEvent.Search -> {
                searchRepositories(event.query.trim(), event.delay)
            }
        }
    }

    private fun getRepositories() {
        getRepositoriesUseCase("google")
            .onEach { result ->
                uiState = uiState.copy(repositoriesResult = result)
            }.launchIn(viewModelScope)
    }

    private fun searchRepositories(
        query: String,
        delay: Long = 0,
    ) {
        searchJob?.cancel()
        searchJob = null

        if (query.isBlank()) {
            uiState = uiState.copy(searchRepositoriesResult = Result.Empty())
            return
        }

        searchJob =
            viewModelScope.launch {
                delay(delay)
                searchRepositoriesUseCase(query)
                    .onEach { result ->
                        uiState = uiState.copy(searchRepositoriesResult = result)
                    }.launchIn(this)
            }
    }
}
