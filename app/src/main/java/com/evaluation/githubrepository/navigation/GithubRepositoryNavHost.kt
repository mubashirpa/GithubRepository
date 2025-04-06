package com.evaluation.githubrepository.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.evaluation.githubrepository.presentation.home.HomeScreen
import com.evaluation.githubrepository.presentation.home.HomeViewModel
import com.evaluation.githubrepository.presentation.login.LoginScreen
import com.evaluation.githubrepository.presentation.repo.RepoScreen
import com.evaluation.githubrepository.presentation.repo.RepoViewModel
import com.evaluation.githubrepository.presentation.settings.SettingsScreen
import com.evaluation.githubrepository.presentation.settings.SettingsViewModel
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
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings)
                },
                onNavigateToRepo = { owner, repo ->
                    navController.navigate(Screen.Repo(owner, repo))
                },
            )
        }
        composable<Screen.Settings> {
            val viewModel: SettingsViewModel = koinViewModel()
            SettingsScreen(
                onEvent = viewModel::onEvent,
                onNavigateUp = navController::navigateUp,
                onSignOutComplete = {
                    navController.navigate(Screen.Login) {
                        popUpTo(Screen.Home) { inclusive = true }
                    }
                },
            )
        }
        composable<Screen.Repo> { backStackEntry ->
            val repo = backStackEntry.toRoute<Screen.Repo>()
            val viewModel: RepoViewModel = koinViewModel()

            RepoScreen(
                uiState = viewModel.uiState,
                onEvent = viewModel::onEvent,
                title = repo.repo,
                subtitle = repo.owner,
                onNavigateUp = navController::navigateUp,
            )
        }
    }
}
