package com.evaluation.githubrepository.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.evaluation.githubrepository.R

@Composable
fun NotificationPermissionDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    open: Boolean,
    modifier: Modifier = Modifier,
) {
    if (open) {
        AlertDialog(
            onDismissRequest = {
                onDismissRequest()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirmation()
                    },
                ) {
                    Text(text = stringResource(R.string.confirm))
                }
            },
            modifier = modifier,
            dismissButton = {
                TextButton(
                    onClick = {
                        onDismissRequest()
                    },
                ) {
                    Text(text = stringResource(R.string.dismiss))
                }
            },
            title = {
                Text(text = stringResource(R.string.notification_permission))
            },
            text = {
                Text(text = stringResource(R.string.notification_permission_is_required_to_show_notifications))
            },
        )
    }
}
