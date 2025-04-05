package com.evaluation.githubrepository.presentation.repo

import com.evaluation.githubrepository.core.Result
import com.evaluation.githubrepository.domain.model.github.repo.RepoDetails

data class RepoUiState(
    val repoResult: Result<RepoDetails> = Result.Empty(),
)
