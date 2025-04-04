package com.evaluation.githubrepository.presentation.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.evaluation.githubrepository.R
import com.evaluation.githubrepository.presentation.settings.components.Preference
import com.evaluation.githubrepository.presentation.theme.GithubRepositoryTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onEvent: (SettingsUiEvent) -> Unit,
    onNavigateUp: () -> Unit,
    onSignOutComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val currentOnSignOutComplete by rememberUpdatedState(onSignOutComplete)

    Column(modifier = modifier) {
        TopAppBar(
            title = {
                Text(text = stringResource(id = R.string.title_settings_screen))
            },
            navigationIcon = {
                IconButton(onClick = onNavigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                    )
                }
            },
            scrollBehavior = scrollBehavior,
        )
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
        ) {
            Preference(
                title = stringResource(R.string.sign_out),
                icon = Icons.AutoMirrored.Default.Logout,
                onClick = {
                    onEvent(SettingsUiEvent.SignOut)
                    currentOnSignOutComplete()
                },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    GithubRepositoryTheme {
        SettingsScreen(
            onEvent = {},
            onNavigateUp = {},
            onSignOutComplete = {},
        )
    }
}
