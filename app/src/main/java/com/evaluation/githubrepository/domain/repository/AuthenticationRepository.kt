package com.evaluation.githubrepository.domain.repository

import com.evaluation.githubrepository.domain.model.UserInfo

interface AuthenticationRepository {
    suspend fun signInWithGoogle(
        token: String,
        nonce: String,
    ): UserInfo?

    fun signOut()
}
