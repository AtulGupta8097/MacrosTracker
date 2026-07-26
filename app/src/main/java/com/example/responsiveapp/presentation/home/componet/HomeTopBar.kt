package com.example.responsiveapp.presentation.home.componet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.R
import com.example.responsiveapp.domain.model.ActivityLevel
import com.example.responsiveapp.domain.model.Gender
import com.example.responsiveapp.domain.model.Goal
import com.example.responsiveapp.domain.model.UserProfile
import com.example.responsiveapp.presentation.ui.theme.ResponsiveAppTheme
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun HomeAppBar(
    modifier: Modifier = Modifier,
    userProfile: UserProfile?,
    onEditProfile: () -> Unit = {},
    onSeeMoreProfile: () -> Unit = {},
) {
    var showProfilePopup by remember {
        mutableStateOf(false)
    }

    BoxWithConstraints(
        modifier = modifier.fillMaxWidth()
    ) {
        val containerMacWidth = maxWidth

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .statusBarsPadding()
                .padding(
                    horizontal = MaterialTheme.spacing.md,
                    vertical = MaterialTheme.spacing.sm
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(32.dp)
                    .clip(
                        RoundedCornerShape(MaterialTheme.spacing.xs)
                    )
            )

            Text(
                text = stringResource(R.string.app_name),
                modifier = Modifier
                    .weight(1f)
                    .padding(start = MaterialTheme.spacing.sm),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            ProfileAvatar(
                name = userProfile?.name.orEmpty(),
                size = 40.dp,
                showBorder = true,
                onClick = {
                    showProfilePopup = true
                }
            )
        }

        if (showProfilePopup) {

            ProfilePopup(
                userProfile = userProfile,
                containerMaxWidth = containerMacWidth,
                onDismiss = {
                    showProfilePopup = false
                },
                onEditProfile = {
                    showProfilePopup = false
                    onEditProfile()
                },
                onSeeMoreProfile = {
                    showProfilePopup = false
                    onSeeMoreProfile()
                }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun HomeAppBarPreview() {
    ResponsiveAppTheme {

        HomeAppBar(
            userProfile = UserProfile(
                name = "Atul",
                gender = Gender.MALE,
                age = 24,
                height = 175f,
                weight = 70f,
                activityLevel = ActivityLevel.MODERATE,
                goal = Goal.MAINTAIN
            )
        )
    }
}