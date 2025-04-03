package com.evaluation.githubrepository.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.evaluation.githubrepository.navigation.GithubRepositoryNavHost
import com.evaluation.githubrepository.presentation.theme.GithubRepositoryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GithubRepositoryTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GithubRepositoryNavHost(
                        navController = rememberNavController(),
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                    )
                }
            }
        }
    }
}
