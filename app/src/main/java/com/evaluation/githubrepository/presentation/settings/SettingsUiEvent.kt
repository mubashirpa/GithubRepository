package com.evaluation.githubrepository.presentation.settings

sealed class SettingsUiEvent {
    data object SignOut : SettingsUiEvent()
}
