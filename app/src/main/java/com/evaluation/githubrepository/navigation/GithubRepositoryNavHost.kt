package com.evaluation.githubrepository.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.evaluation.githubrepository.presentation.home.HomeScreen
import com.evaluation.githubrepository.presentation.home.HomeViewModel
import com.evaluation.githubrepository.presentation.login.LoginScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun GithubRepositoryNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: Screen = Screen.Login,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable<Screen.Login> {
            LoginScreen(
                onLoginComplete = {
                    navController.navigate(Screen.Home) {
                        popUpTo(Screen.Login) { inclusive = true }
                    }
                },
            )
        }
        composable<Screen.Home> {
            val viewModel: HomeViewModel = koinViewModel()
            HomeScreen(
                uiState = viewModel.uiState,
                onEvent = viewModel::onEvent,
            )
        }
    }
}
