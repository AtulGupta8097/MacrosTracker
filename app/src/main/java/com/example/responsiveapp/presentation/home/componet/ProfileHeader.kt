package com.example.responsiveapp.presentation.home.componet
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.domain.model.UserProfile
import com.example.responsiveapp.presentation.ui.theme.spacing
private val AvatarSize = 52.dp

@Composable
fun ProfileHeader(
    userProfile: UserProfile?,
    modifier: Modifier = Modifier,
) {
    val displayName =
        userProfile?.name?.ifBlank { null }
            ?: "Your Profile"

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        ProfileAvatar(
            name = userProfile?.name,
            size = AvatarSize
        )

        Column(
            modifier = Modifier.padding(
                start = MaterialTheme.spacing.sm
            )
        ) {

            Text(
                text = displayName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )

            userProfile?.let { profile ->

                Text(
                    text = "${profile.age} yrs · ${profile.height.toInt()} cm · ${profile.weight.toInt()} kg",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}