package com.evaluation.githubrepository.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.evaluation.githubrepository.R
import com.evaluation.githubrepository.presentation.components.ErrorScreen
import com.evaluation.githubrepository.presentation.components.LoadingScreen
import com.evaluation.githubrepository.presentation.home.components.HomeSearchBar
import com.evaluation.githubrepository.presentation.home.components.RepoListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToRepo: (Int) -> Unit,
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
                onNavigateToSettings = onNavigateToSettings,
                onNavigateToRepo = onNavigateToRepo,
            )
        },
    ) { innerPadding ->
        val lazyPagingItems = uiState.repositories.collectAsLazyPagingItems()

        when (lazyPagingItems.loadState.refresh) {
            is LoadState.Error -> {
                val message = stringResource(R.string.error_unknown)
                ErrorScreen(
                    onRetryClick = {
                        lazyPagingItems.refresh()
                    },
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                    errorMessage = message,
                )
            }

            LoadState.Loading -> {
                LoadingScreen(modifier = Modifier.padding(innerPadding))
            }

            is LoadState.NotLoading -> {
                val layoutDirection = LocalLayoutDirection.current
                val contentPadding =
                    PaddingValues(
                        top = 4.dp,
                        bottom = 12.dp + innerPadding.calculateBottomPadding(),
                        start = 16.dp + innerPadding.calculateStartPadding(layoutDirection),
                        end = 16.dp + innerPadding.calculateEndPadding(layoutDirection),
                    )

                PullToRefreshBox(
                    isRefreshing = uiState.isRefreshing,
                    onRefresh = {
                        lazyPagingItems.refresh()
                    },
                    modifier = Modifier.padding(top = innerPadding.calculateTopPadding()),
                ) {
                    LazyColumn(
                        contentPadding = contentPadding,
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        items(
                            count = lazyPagingItems.itemCount,
                            key = lazyPagingItems.itemKey(),
                            contentType = lazyPagingItems.itemContentType(),
                        ) { index ->
                            lazyPagingItems[index]?.let { repo ->
                                RepoListItem(
                                    onClick = {
                                        repo.id?.let(onNavigateToRepo)
                                    },
                                    title = repo.name.orEmpty(),
                                    description = repo.description.orEmpty(),
                                    starCount = repo.stargazersCount ?: 0,
                                    modifier = Modifier.animateItem(),
                                )
                            }
                        }

                        when (lazyPagingItems.loadState.append) {
                            is LoadState.Error -> {
                                item {
                                    val message = stringResource(R.string.error_unknown)
                                    ErrorScreen(
                                        onRetryClick = {
                                            lazyPagingItems.retry()
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        errorMessage = message,
                                    )
                                }
                            }

                            LoadState.Loading -> {
                                item {
                                    LoadingScreen()
                                }
                            }

                            is LoadState.NotLoading -> {}
                        }
                    }
                }
            }
        }
    }
}
