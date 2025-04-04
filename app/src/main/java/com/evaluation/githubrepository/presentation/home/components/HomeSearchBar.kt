package com.evaluation.githubrepository.presentation.home.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.ExpandedFullScreenSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarScrollBehavior
import androidx.compose.material3.SearchBarValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopSearchBar
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.evaluation.githubrepository.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeSearchBar(
    scrollBehavior: SearchBarScrollBehavior,
    onFilterClick: () -> Unit,
) {
    val textFieldState = rememberTextFieldState()
    val searchBarState = rememberSearchBarState()
    val scope = rememberCoroutineScope()

    val inputField =
        @Composable {
            SearchBarDefaults.InputField(
                modifier = Modifier,
                searchBarState = searchBarState,
                textFieldState = textFieldState,
                onSearch = {
                    scope.launch { searchBarState.animateToCollapsed() }
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
                        if (textFieldState.text.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    textFieldState.clearText()
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = null,
                                )
                            }
                        }
                    } else {
                        IconButton(onClick = onFilterClick) {
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
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(100) {
                ListItem(
                    headlineContent = {
                        Text("$it")
                    },
                    colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                )
            }
        }
    }
}
