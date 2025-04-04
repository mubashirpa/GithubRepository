package com.evaluation.githubrepository.domain.usecase

import com.evaluation.githubrepository.R
import com.evaluation.githubrepository.core.Result
import com.evaluation.githubrepository.core.UiText
import com.evaluation.githubrepository.domain.model.UserInfo
import com.evaluation.githubrepository.domain.repository.AuthenticationRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class LoginWithGoogleUseCase(
    private val repository: AuthenticationRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    operator fun invoke(
        token: String,
        nonce: String,
    ): Flow<Result<UserInfo>> =
        flow {
            try {
                emit(Result.Loading())
                val userInfo = repository.signInWithGoogle(token, nonce)
                if (userInfo != null) {
                    emit(Result.Success(userInfo))
                } else {
                    emit(Result.Error(UiText.StringResource(R.string.error_unexpected)))
                }
            } catch (e: Exception) {
                val message =
                    e.message?.let { message -> UiText.DynamicString(message) }
                        ?: UiText.StringResource(R.string.error_unknown)
                emit(Result.Error(message))
            }
        }.flowOn(ioDispatcher)
}
