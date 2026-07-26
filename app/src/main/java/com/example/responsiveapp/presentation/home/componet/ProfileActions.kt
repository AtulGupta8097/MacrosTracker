package com.example.responsiveapp.presentation.home.componet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.presentation.ui.theme.spacing

private val ActionIconSize = 16.dp

@Composable
fun ProfileActions(
    onEditProfile: () -> Unit,
    onSeeMoreProfile: () -> Unit,
    modifier: Modifier = Modifier,
) {

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(
            MaterialTheme.spacing.sm
        )
    ) {

        OutlinedButton(
            modifier = Modifier.weight(1f),
            onClick = onEditProfile
        ) {

            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null,
                modifier = Modifier.size(ActionIconSize)
            )

            Text(
                text = "Edit",
                modifier = Modifier.padding(
                    start = MaterialTheme.spacing.xs
                ),
                style = MaterialTheme.typography.labelMedium
            )
        }

        Button(
            modifier = Modifier.weight(1f),
            onClick = onSeeMoreProfile,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {

            Text(
                text = "See more",
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}