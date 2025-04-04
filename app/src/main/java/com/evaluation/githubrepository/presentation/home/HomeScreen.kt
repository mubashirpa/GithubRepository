package com.evaluation.githubrepository.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import com.evaluation.githubrepository.core.Result
import com.evaluation.githubrepository.presentation.components.ErrorScreen
import com.evaluation.githubrepository.presentation.components.LoadingScreen
import com.evaluation.githubrepository.presentation.home.components.HomeSearchBar
import com.evaluation.githubrepository.presentation.home.components.RepoListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = SearchBarDefaults.enterAlwaysSearchBarScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            HomeSearchBar(
                scrollBehavior = scrollBehavior,
                uiState = uiState,
                onEvent = onEvent,
                onFilterClick = { /*TODO*/ },
            )
        },
    ) { innerPadding ->
        when (val repositoriesResult = uiState.repositoriesResult) {
            is Result.Empty -> {}

            is Result.Error -> {
                ErrorScreen(
                    onRetryClick = {
                        onEvent(HomeUiEvent.Retry)
                    },
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                    errorMessage = repositoriesResult.message!!.asString(),
                )
            }

            is Result.Loading -> {
                LoadingScreen(modifier = Modifier.padding(innerPadding))
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
