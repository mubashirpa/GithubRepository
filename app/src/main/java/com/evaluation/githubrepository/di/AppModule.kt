package com.evaluation.githubrepository.di

import com.evaluation.githubrepository.data.repository.AuthenticationRepositoryImpl
import com.evaluation.githubrepository.data.repository.GitHubRepositoryImpl
import com.evaluation.githubrepository.domain.repository.AuthenticationRepository
import com.evaluation.githubrepository.domain.repository.GitHubRepository
import com.evaluation.githubrepository.domain.usecase.IsUserLoggedInUseCase
import com.evaluation.githubrepository.domain.usecase.LoginWithGoogleUseCase
import com.evaluation.githubrepository.presentation.login.LoginViewModel
import com.evaluation.githubrepository.presentation.main.MainViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule =
    module {
        single { Firebase.auth }
        singleOf(::AuthenticationRepositoryImpl) { bind<AuthenticationRepository>() }
        singleOf(::GitHubRepositoryImpl) { bind<GitHubRepository>() }
        single { LoginWithGoogleUseCase(get()) }
        singleOf(::IsUserLoggedInUseCase)
        viewModelOf(::LoginViewModel)
        viewModelOf(::MainViewModel)
        single {
            HttpClient {
                expectSuccess = true
                install(ContentNegotiation) {
                    json(
                        Json {
                            isLenient = true
                            ignoreUnknownKeys = true
                            useAlternativeNames = false
                        },
                    )
                }
            }
        }
    }
