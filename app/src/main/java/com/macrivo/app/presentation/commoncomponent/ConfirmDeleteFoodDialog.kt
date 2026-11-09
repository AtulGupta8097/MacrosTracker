package com.macrivo.app.presentation.commoncomponent

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

// CHANGED: new file. Confirmation before a logged food is removed.
@Composable
fun ConfirmDeleteFoodDialog(
    foodName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    isDeleting: Boolean = false,
) {
    AlertDialog(
        onDismissRequest = { if (!isDeleting) onDismiss() },
        title = { Text("Delete food?") },
        text = {
            Text(
                "\"$foodName\" will be removed from your diary and your daily totals will be updated."
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm, enabled = !isDeleting) {
                Text("Delete", color = MaterialTheme.colorScheme.error)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, enabled = !isDeleting) {
                Text("Cancel")
            }
        },
    )
}
