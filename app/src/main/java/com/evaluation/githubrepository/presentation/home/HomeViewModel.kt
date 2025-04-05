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
    private var repoJob: Job? = null

    init {
        getRepositories(false)
        collectSearchQuery()
    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            HomeUiEvent.Refresh -> {
                getRepositories(true)
            }

            HomeUiEvent.Retry -> {
                getRepositories(false)
            }

            is HomeUiEvent.Search -> {
                searchRepositories(event.query.trim(), event.delay)
            }

            HomeUiEvent.UserMessageShown -> {
                uiState = uiState.copy(userMessage = null)
            }
        }
    }

    private fun getRepositories(isRefreshing: Boolean) {
        // Cancel any ongoing repoJob before making a new call.
        repoJob?.cancel()
        repoJob =
            getRepositoriesUseCase("google")
                .onEach { result ->
                    when (result) {
                        is Result.Empty -> {}

                        is Result.Error -> {
                            // The Result.Error state is only used during initial loading and retry attempts.
                            // Otherwise, a snackbar is displayed using the userMessage property.
                            uiState =
                                if (isRefreshing) {
                                    uiState.copy(
                                        isRefreshing = false,
                                        userMessage = result.message,
                                    )
                                } else {
                                    uiState.copy(repositoriesResult = result)
                                }
                        }

                        is Result.Loading -> {
                            // The Result.Loading state is only used during initial loading and retry attempts.
                            // In other cases, the PullRefreshIndicator is shown with isRefreshing = true.
                            uiState =
                                if (isRefreshing) {
                                    uiState.copy(isRefreshing = true)
                                } else {
                                    uiState.copy(repositoriesResult = result)
                                }
                        }

                        is Result.Success -> {
                            uiState =
                                uiState.copy(
                                    repositoriesResult = result,
                                    isRefreshing = false,
                                )
                        }
                    }
                }.launchIn(viewModelScope)
    }

    private fun searchRepositories(
        query: String,
        delay: Long = 0,
    ) {
        // Cancel any ongoing searchJob before making a new call.
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

    private fun collectSearchQuery() {
        viewModelScope.launch {
            snapshotFlow { uiState.searchFieldState.text }.collectLatest { queryText ->
                // Start a new search every time the user types something.
                searchRepositories(queryText.toString(), 500)
            }
        }
    }
}
