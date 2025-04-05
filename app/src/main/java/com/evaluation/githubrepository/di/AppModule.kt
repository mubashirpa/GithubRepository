package com.evaluation.githubrepository.di

import androidx.room.Room
import com.evaluation.githubrepository.data.local.database.GithubDatabase
import com.evaluation.githubrepository.data.repository.AuthenticationRepositoryImpl
import com.evaluation.githubrepository.data.repository.GithubRepositoryImpl
import com.evaluation.githubrepository.domain.repository.AuthenticationRepository
import com.evaluation.githubrepository.domain.repository.GitHubRepository
import com.evaluation.githubrepository.domain.usecase.GetRepositoriesPagingUseCase
import com.evaluation.githubrepository.domain.usecase.GetRepositoriesUseCase
import com.evaluation.githubrepository.domain.usecase.IsUserLoggedInUseCase
import com.evaluation.githubrepository.domain.usecase.LoginWithGoogleUseCase
import com.evaluation.githubrepository.domain.usecase.SearchRepositoriesUseCase
import com.evaluation.githubrepository.domain.usecase.SignOutUseCase
import com.evaluation.githubrepository.presentation.home.HomeViewModel
import com.evaluation.githubrepository.presentation.login.LoginViewModel
import com.evaluation.githubrepository.presentation.main.MainViewModel
import com.evaluation.githubrepository.presentation.settings.SettingsViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule =
    module {
        single { Firebase.auth }
        singleOf(::AuthenticationRepositoryImpl) { bind<AuthenticationRepository>() }
        singleOf(::GithubRepositoryImpl) { bind<GitHubRepository>() }
        single { LoginWithGoogleUseCase(get()) }
        single { GetRepositoriesUseCase(get()) }
        single { SearchRepositoriesUseCase(get()) }
        single { GetRepositoriesPagingUseCase(get()) }
        singleOf(::IsUserLoggedInUseCase)
        singleOf(::SignOutUseCase)
        viewModelOf(::LoginViewModel)
        viewModelOf(::MainViewModel)
        viewModelOf(::HomeViewModel)
        viewModelOf(::SettingsViewModel)
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
        single {
            Room.databaseBuilder(androidContext(), GithubDatabase::class.java, "github_db").build()
        }
    }
