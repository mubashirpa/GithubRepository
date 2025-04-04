package com.evaluation.githubrepository.presentation.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import com.evaluation.githubrepository.presentation.theme.GithubRepositoryTheme

@Composable
fun SearchListItem(
    onClick: () -> Unit,
    title: String,
    description: String,
    starCount: Int,
    modifier: Modifier = Modifier,
) {
    ListItem(
        headlineContent = {
            Text(
                text = title,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        },
        modifier = modifier.clickable(onClick = onClick),
        supportingContent =
            if (description.isNotBlank()) {
                {
                    Text(
                        text = description,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2,
                    )
                }
            } else {
                null
            },
        trailingContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color(0xFFFFE234),
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = starCount.toString())
            }
        },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
    )
}

@Preview
@Composable
private fun SearchListItemPreview(
    @PreviewParameter(LoremIpsum::class) description: String,
) {
    GithubRepositoryTheme {
        SearchListItem(
            onClick = {},
            title = "Title",
            description = description,
            starCount = 10,
        )
    }
}
