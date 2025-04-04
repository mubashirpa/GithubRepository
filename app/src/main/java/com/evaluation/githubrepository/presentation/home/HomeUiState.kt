package com.evaluation.githubrepository.presentation.home

import androidx.compose.foundation.text.input.TextFieldState
import com.evaluation.githubrepository.core.Result
import com.evaluation.githubrepository.domain.model.github.repos.Repo
import com.evaluation.githubrepository.domain.model.github.search.SearchRepo

data class HomeUiState(
    val repositoriesResult: Result<List<Repo>> = Result.Empty(),
    val searchRepositoriesResult: Result<List<SearchRepo>> = Result.Empty(),
    val searchFieldState: TextFieldState = TextFieldState(),
)
