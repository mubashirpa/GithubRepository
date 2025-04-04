package com.evaluation.githubrepository.domain.usecase

import com.evaluation.githubrepository.domain.repository.AuthenticationRepository

class SignOutUseCase(
    private val repository: AuthenticationRepository,
) {
    operator fun invoke() {
        repository.signOut()
    }
}
