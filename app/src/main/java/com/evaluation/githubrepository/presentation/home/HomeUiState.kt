package com.evaluation.githubrepository.presentation.home

import androidx.compose.foundation.text.input.TextFieldState
import androidx.paging.PagingData
import com.evaluation.githubrepository.core.Result
import com.evaluation.githubrepository.core.UiText
import com.evaluation.githubrepository.domain.model.github.repos.Repo
import com.evaluation.githubrepository.domain.model.github.search.SearchRepo
import kotlinx.coroutines.flow.MutableStateFlow

data class HomeUiState(
    val isRefreshing: Boolean = false,
    val notificationPermissionRequested: Boolean = false,
    val openNotificationPermissionDialog: Boolean = false,
    val repositories: MutableStateFlow<PagingData<Repo>> = MutableStateFlow(PagingData.empty()),
    val searchRepositoriesResult: Result<List<SearchRepo>> = Result.Empty(),
    val searchFieldState: TextFieldState = TextFieldState(),
    val userMessage: UiText? = null,
)
