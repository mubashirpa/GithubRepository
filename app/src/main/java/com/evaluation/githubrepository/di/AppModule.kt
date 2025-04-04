package com.evaluation.githubrepository.di

import com.evaluation.githubrepository.data.repository.AuthenticationRepositoryImpl
import com.evaluation.githubrepository.domain.repository.AuthenticationRepository
import com.evaluation.githubrepository.domain.usecase.IsUserLoggedInUseCase
import com.evaluation.githubrepository.domain.usecase.LoginWithGoogleUseCase
import com.evaluation.githubrepository.presentation.login.LoginViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule =
    module {
        single { Firebase.auth }
        singleOf(::AuthenticationRepositoryImpl) { bind<AuthenticationRepository>() }
        single { LoginWithGoogleUseCase(get()) }
        singleOf(::IsUserLoggedInUseCase)
        viewModelOf(::LoginViewModel)
    }
