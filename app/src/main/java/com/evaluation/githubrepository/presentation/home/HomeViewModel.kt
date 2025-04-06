package com.evaluation.githubrepository.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.evaluation.githubrepository.core.Result
import com.evaluation.githubrepository.domain.usecase.GetRepositoriesPagingUseCase
import com.evaluation.githubrepository.domain.usecase.SearchRepositoriesUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getRepositoriesPagingUseCase: GetRepositoriesPagingUseCase,
    private val searchRepositoriesUseCase: SearchRepositoriesUseCase,
) : ViewModel() {
    var uiState by mutableStateOf(HomeUiState())
        private set

    private var searchJob: Job? = null

    init {
        getRepositories()
        collectSearchQuery()
    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.OnNotificationPermissionRequestedChange -> {
                uiState = uiState.copy(notificationPermissionRequested = event.requested)
            }

            is HomeUiEvent.OnOpenNotificationPermissionDialogChange -> {
                uiState = uiState.copy(openNotificationPermissionDialog = event.open)
            }

            is HomeUiEvent.Search -> {
                searchRepositories(event.query.trim(), event.delay)
            }

            HomeUiEvent.UserMessageShown -> {
                uiState = uiState.copy(userMessage = null)
            }
        }
    }

    private fun getRepositories() {
        viewModelScope.launch {
            getRepositoriesPagingUseCase("mubashirpa")
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collect {
                    uiState.repositories.value = it
                }
        }
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
