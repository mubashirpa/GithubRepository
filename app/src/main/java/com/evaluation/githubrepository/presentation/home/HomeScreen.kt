package com.evaluation.githubrepository.presentation.home

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.evaluation.githubrepository.presentation.home.components.HomeSearchBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val scrollBehavior = SearchBarDefaults.enterAlwaysSearchBarScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            HomeSearchBar(scrollBehavior = scrollBehavior)
        },
    ) { innerPadding ->
        LazyColumn(contentPadding = innerPadding) {
            items(100) {
                ListItem(
                    headlineContent = {
                        Text("$it")
                    },
                )
            }
        }
    }
}
