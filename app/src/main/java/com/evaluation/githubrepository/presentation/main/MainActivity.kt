package com.evaluation.githubrepository.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.evaluation.githubrepository.navigation.GithubRepositoryNavHost
import com.evaluation.githubrepository.navigation.Screen
import com.evaluation.githubrepository.presentation.components.LoadingScreen
import com.evaluation.githubrepository.presentation.theme.GithubRepositoryTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition { viewModel.uiState.isLoading }

        enableEdgeToEdge()
        setContent {
            GithubRepositoryTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (viewModel.uiState.isLoading) {
                        LoadingScreen(modifier = Modifier.padding(innerPadding))
                    } else {
                        val startDestination =
                            if (viewModel.uiState.isUserLoggedIn) Screen.Home else Screen.Home
                        GithubRepositoryNavHost(
                            navController = rememberNavController(),
                            modifier =
                                Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding),
                            startDestination = startDestination,
                        )
                    }
                }
            }
        }
    }
}
