package com.example.responsiveapp.presentation.home.componet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.presentation.ui.theme.spacing

private val ActionIconSize = 16.dp
private val ActionButtonShape = RoundedCornerShape(10.dp)

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
            onClick = onEditProfile,
            shape = ActionButtonShape,
            contentPadding = PaddingValues(
                horizontal = MaterialTheme.spacing.sm,
                vertical = ButtonDefaults.ContentPadding.calculateTopPadding()
            )
        ) {

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {

                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(ActionIconSize)
                )

                Text(
                    text = "Edit Profile",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1
                )
            }
        }

        Button(
            modifier = Modifier.weight(1f),
            onClick = onSeeMoreProfile,
            shape = ActionButtonShape,
            contentPadding = PaddingValues(
                horizontal = MaterialTheme.spacing.sm,
                vertical = ButtonDefaults.ContentPadding.calculateTopPadding()
            ),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = "View Details",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1
                )

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(ActionIconSize)
                )
            }
        }
    }
}