package com.evaluation.githubrepository.domain.usecase

import com.evaluation.githubrepository.domain.repository.AuthenticationRepository

class IsUserLoggedInUseCase(
    private val repository: AuthenticationRepository,
) {
    operator fun invoke(): Boolean = repository.isUserLoggedIn
}
