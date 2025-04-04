package com.evaluation.githubrepository.domain.repository

import com.evaluation.githubrepository.domain.model.auth.UserInfo

interface AuthenticationRepository {
    val isUserLoggedIn: Boolean

    suspend fun signInWithGoogle(
        token: String,
        nonce: String,
    ): UserInfo?

    fun signOut()
}
