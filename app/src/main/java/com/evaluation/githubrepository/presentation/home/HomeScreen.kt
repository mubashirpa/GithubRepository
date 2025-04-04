package com.evaluation.githubrepository.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import com.evaluation.githubrepository.core.Result
import com.evaluation.githubrepository.presentation.components.LoadingScreen
import com.evaluation.githubrepository.presentation.home.components.HomeSearchBar
import com.evaluation.githubrepository.presentation.home.components.RepoListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = SearchBarDefaults.enterAlwaysSearchBarScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            HomeSearchBar(
                scrollBehavior = scrollBehavior,
                onFilterClick = { /*TODO*/ },
            )
        },
    ) { innerPadding ->
        when (val repositoriesResult = uiState.repositoriesResult) {
            is Result.Empty -> {}

            is Result.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = repositoriesResult.message!!.asString())
                }
            }

            is Result.Loading -> {
                LoadingScreen()
            }

            is Result.Success -> {
                val repositories = repositoriesResult.data!!
                val layoutDirection = LocalLayoutDirection.current
                val contentPadding =
                    PaddingValues(
                        top = 4.dp + innerPadding.calculateTopPadding(),
                        bottom = 12.dp + innerPadding.calculateBottomPadding(),
                        start = 16.dp + innerPadding.calculateStartPadding(layoutDirection),
                        end = 16.dp + innerPadding.calculateEndPadding(layoutDirection),
                    )

                LazyColumn(
                    contentPadding = contentPadding,
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    items(
                        items = repositories,
                        key = { repo -> repo.id!! },
                    ) { repo ->
                        RepoListItem(
                            onClick = {},
                            title = repo.name.orEmpty(),
                            description = repo.description.orEmpty(),
                            starCount = repo.stargazersCount ?: 0,
                            modifier = Modifier.animateItem(),
                        )
                    }
                }
            }
        }
    }
}
