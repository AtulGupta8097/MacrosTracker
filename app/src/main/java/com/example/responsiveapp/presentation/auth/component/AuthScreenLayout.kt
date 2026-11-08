package com.example.responsiveapp.presentation.auth.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.R
import com.example.responsiveapp.presentation.ui.theme.DeviceConfiguration
import com.example.responsiveapp.presentation.ui.theme.deviceConfiguration
import com.example.responsiveapp.presentation.ui.theme.spacing

private val SheetCorner = 28.dp

@Composable
fun AuthScreenLayout(
    heading: String,
    subHeading: String,
    modifier: Modifier = Modifier,
    overlay: @Composable () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (MaterialTheme.deviceConfiguration == DeviceConfiguration.MOBILE) {
            CompactAuthLayout(heading, subHeading, content)
        } else {
            WideAuthLayout(heading, subHeading, content)
        }

        overlay()
    }
}

@Composable
private fun CompactAuthLayout(
    heading: String,
    subHeading: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val heroHeight = (maxHeight * 0.34f).coerceIn(240.dp, 320.dp)
        val headerHeight = heroHeight - SheetCorner

        AuthHeroImage(modifier = Modifier
            .fillMaxWidth()
            .height(heroHeight))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(headerHeight),
                contentAlignment = Alignment.BottomStart
            ) {
                AuthHeader(
                    heading = heading,
                    subHeading = subHeading,
                    modifier = Modifier
                        .padding(horizontal = MaterialTheme.spacing.lg)
                        .padding(bottom = MaterialTheme.spacing.md)
                )
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(
                        min = this@BoxWithConstraints.maxHeight - headerHeight
                    ),
                shape = RoundedCornerShape(topStart = SheetCorner, topEnd = SheetCorner),
                color = MaterialTheme.colorScheme.background
            ) {
                Column(
                    modifier = Modifier
                        .widthIn(max = 480.dp)
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = MaterialTheme.spacing.lg)
                        .padding(top = MaterialTheme.spacing.lg, bottom = MaterialTheme.spacing.lg),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.md),
                    content = content
                )
            }
        }
    }
}

@Composable
private fun WideAuthLayout(
    heading: String,
    subHeading: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    Row(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            AuthHeroImage(modifier = Modifier.fillMaxSize())
            AuthHeader(
                heading = heading,
                subHeading = subHeading,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(MaterialTheme.spacing.xl),
                isLarge = true
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .imePadding()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 440.dp)
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(MaterialTheme.spacing.xl),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.md),
                content = content
            )
        }
    }
}

@Composable
private fun AuthHeroImage(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Image(
            painter = painterResource(id = R.drawable.chest_press_machine_img),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Black.copy(alpha = 0.35f), Color.Black.copy(alpha = 0.8f))
                    )
                )
        )
    }
}

@Composable
private fun AuthHeader(
    heading: String,
    subHeading: String,
    modifier: Modifier = Modifier,
    isLarge: Boolean = false,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm)
    ) {
        Box(
            modifier = Modifier
                .size(if (isLarge) 64.dp else 52.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = stringResource(R.string.app_name),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            )
        }

        Text(
            text = heading,
            style = (if (isLarge) MaterialTheme.typography.displaySmall else MaterialTheme.typography.headlineMedium)
                .copy(fontWeight = FontWeight.Bold),
            color = Color.White
        )

        Text(
            text = subHeading,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.85f)
        )
    }
}
