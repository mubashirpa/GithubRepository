package com.evaluation.githubrepository.presentation.home

import com.evaluation.githubrepository.core.Result
import com.evaluation.githubrepository.domain.model.github.repos.Repo

data class HomeUiState(
    val repositoriesResult: Result<List<Repo>> = Result.Empty(),
)
