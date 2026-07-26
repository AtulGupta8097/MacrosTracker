package com.example.responsiveapp.presentation.home.componet
import androidx.compose.foundation.layout.Arrangement
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
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.md)
    ) {

        ProfileAvatar(
            name = userProfile?.name,
            size = AvatarSize,
            showBorder = true
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
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                maxLines = 1
            )

            Text(
                text = "Personal Profile",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}