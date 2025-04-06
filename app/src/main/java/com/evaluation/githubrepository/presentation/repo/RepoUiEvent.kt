package com.evaluation.githubrepository.presentation.repo

sealed class RepoUiEvent {
    data object Retry : RepoUiEvent()
}
