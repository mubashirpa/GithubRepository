package com.evaluation.githubrepository.presentation.repo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumFlexibleTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import com.evaluation.githubrepository.R
import com.evaluation.githubrepository.core.Result
import com.evaluation.githubrepository.domain.model.github.repo.RepoDetails
import com.evaluation.githubrepository.presentation.components.ErrorScreen
import com.evaluation.githubrepository.presentation.components.LoadingScreen
import com.evaluation.githubrepository.presentation.theme.GithubRepositoryTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun RepoScreen(
    uiState: RepoUiState,
    title: String,
    subtitle: String,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumFlexibleTopAppBar(
                title = {
                    Text(
                        text = title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                subtitle = {
                    Text(
                        text = subtitle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->
        when (val repoResult = uiState.repoResult) {
            is Result.Empty -> {}

            is Result.Error -> {
                ErrorScreen(
                    onRetryClick = { /*TODO*/ },
                    modifier = Modifier.fillMaxSize(),
                    errorMessage = repoResult.message!!.asString(),
                )
            }

            is Result.Loading -> {
                LoadingScreen()
            }

            is Result.Success -> {
                val repo = repoResult.data!!
                val description =
                    repo.description ?: stringResource(R.string.no_description_provided)
                val language = repo.language ?: stringResource(R.string.unknown)
                val starCount = repo.stargazersCount ?: 0

                Column(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(innerPadding)
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                ) {
                    Text(
                        text = stringResource(R.string.description),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        text = description,
                        color = MaterialTheme.colorScheme.onSurface,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 5,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    ChipItem(
                        text = language,
                        icon = Icons.Outlined.Code,
                        modifier = Modifier.padding(top = 16.dp),
                    )
                    ChipItem(
                        text = pluralStringResource(R.plurals.stars, starCount, starCount),
                        icon = Icons.Outlined.StarOutline,
                        modifier = Modifier.padding(top = 8.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun ChipItem(
    text: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = text)
    }
}

@Preview
@Composable
private fun RepoScreenPreview(
    @PreviewParameter(LoremIpsum::class) description: String,
) {
    GithubRepositoryTheme {
        RepoScreen(
            uiState =
                RepoUiState(
                    repoResult =
                        Result.Success(
                            RepoDetails(description = description),
                        ),
                ),
            title = "kotlin",
            subtitle = "JetBrains",
            onNavigateUp = {},
        )
    }
}
