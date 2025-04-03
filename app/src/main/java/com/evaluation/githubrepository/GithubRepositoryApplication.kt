package com.evaluation.githubrepository

import android.app.Application
import com.evaluation.githubrepository.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class GithubRepositoryApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@GithubRepositoryApplication)
            modules(appModule)
        }
    }
}
