package com.evaluation.githubrepository.presentation.home.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.ExpandedFullScreenSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarScrollBehavior
import androidx.compose.material3.SearchBarValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopSearchBar
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import com.evaluation.githubrepository.R
import com.evaluation.githubrepository.core.Result
import com.evaluation.githubrepository.presentation.components.ErrorScreen
import com.evaluation.githubrepository.presentation.components.LoadingScreen
import com.evaluation.githubrepository.presentation.home.HomeUiEvent
import com.evaluation.githubrepository.presentation.home.HomeUiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeSearchBar(
    scrollBehavior: SearchBarScrollBehavior,
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
    onNavigateToSettings: () -> Unit,
) {
    val searchBarState = rememberSearchBarState()
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    val inputField =
        @Composable {
            SearchBarDefaults.InputField(
                modifier = Modifier,
                searchBarState = searchBarState,
                textFieldState = uiState.searchFieldState,
                onSearch = {
                    keyboardController?.hide()
                },
                placeholder = {
                    if (searchBarState.currentValue == SearchBarValue.Expanded) {
                        Text(text = stringResource(R.string.search))
                    } else {
                        Text(text = stringResource(R.string.app_name))
                    }
                },
                leadingIcon = {
                    if (searchBarState.currentValue == SearchBarValue.Expanded) {
                        IconButton(
                            onClick = {
                                scope.launch { searchBarState.animateToCollapsed() }
                            },
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = stringResource(R.string.back),
                            )
                        }
                    } else {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                        )
                    }
                },
                trailingIcon = {
                    if (searchBarState.currentValue == SearchBarValue.Expanded) {
                        if (uiState.searchFieldState.text.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    uiState.searchFieldState.clearText()
                                    onEvent(HomeUiEvent.Search(""))
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = null,
                                )
                            }
                        }
                    } else {
                        IconButton(onClick = onNavigateToSettings) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = null,
                            )
                        }
                    }
                },
            )
        }

    TopSearchBar(
        scrollBehavior = scrollBehavior,
        state = searchBarState,
        inputField = inputField,
    )
    ExpandedFullScreenSearchBar(
        state = searchBarState,
        inputField = inputField,
    ) {
        when (val searchRepositoriesResult = uiState.searchRepositoriesResult) {
            is Result.Empty -> {}

            is Result.Error -> {
                ErrorScreen(
                    onRetryClick = {
                        onEvent(HomeUiEvent.Search(uiState.searchFieldState.text.toString()))
                    },
                    modifier = Modifier.fillMaxSize(),
                    errorMessage = searchRepositoriesResult.message!!.asString(),
                )
            }

            is Result.Loading -> {
                LoadingScreen()
            }

            is Result.Success -> {
                val repositories = searchRepositoriesResult.data!!
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(
                        items = repositories,
                        key = { it.id!! },
                    ) {
                        SearchListItem(
                            onClick = { /*TODO*/ },
                            title = it.name.orEmpty(),
                            description = it.description.orEmpty(),
                            starCount = it.stargazersCount ?: 0,
                            modifier = Modifier.animateItem(),
                        )
                    }
                }
            }
        }
    }
}
