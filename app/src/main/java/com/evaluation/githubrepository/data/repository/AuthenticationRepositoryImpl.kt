package com.evaluation.githubrepository.data.repository

import com.evaluation.githubrepository.domain.model.auth.UserInfo
import com.evaluation.githubrepository.domain.repository.AuthenticationRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

class AuthenticationRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
) : AuthenticationRepository {
    override val isUserLoggedIn: Boolean
        get() = firebaseAuth.currentUser != null

    override suspend fun signInWithGoogle(
        token: String,
        nonce: String,
    ): UserInfo? {
        val credential = GoogleAuthProvider.getCredential(token, nonce)
        val result = firebaseAuth.signInWithCredential(credential).await()
        return result.user?.let { user ->
            UserInfo(
                email = user.email,
                id = user.uid,
                name = user.displayName,
                photoUrl = user.photoUrl?.toString(),
            )
        }
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }
}
