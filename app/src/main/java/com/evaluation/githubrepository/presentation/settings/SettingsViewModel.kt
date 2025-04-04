package com.evaluation.githubrepository.presentation.settings

import androidx.lifecycle.ViewModel
import com.evaluation.githubrepository.domain.usecase.SignOutUseCase

class SettingsViewModel(
    private val signOutUseCase: SignOutUseCase,
) : ViewModel() {
    fun onEvent(event: SettingsUiEvent) {
        when (event) {
            is SettingsUiEvent.SignOut -> {
                signOut()
            }
        }
    }

    private fun signOut() {
        signOutUseCase()
    }
}
