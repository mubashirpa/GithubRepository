package com.evaluation.githubrepository.presentation.home

sealed class HomeUiEvent {
    data class Search(
        val query: String,
        val delay: Long = 0,
    ) : HomeUiEvent()

    data object UserMessageShown : HomeUiEvent()
}
