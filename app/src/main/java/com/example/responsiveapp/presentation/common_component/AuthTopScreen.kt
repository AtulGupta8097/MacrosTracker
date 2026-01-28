package com.example.responsiveapp.presentation.common_component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.painter.Painter
import com.example.responsiveapp.R
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun AuthTopScreen(
    backgroundPainter: Painter = painterResource(id = R.drawable.chest_press_machine_img),
    logoPainter: Painter = painterResource(id = R.drawable.logo),
    heading: String = stringResource(R.string.sign_in_title),
    subHeading: String = stringResource(R.string.sign_in_top_subTitle),
    height: Dp = 260.dp,
    horizontalPadding: Dp = 16.dp,
    circleSize: Dp = 58.dp
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
    ) {
        Image(
            painter = backgroundPainter,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Scrim overlay (transparent -> darker at bottom)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.background                       )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(start = horizontalPadding, end = horizontalPadding, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo inside rounded colored box
            Box(
                modifier = Modifier
                    .size(circleSize)
                    .clip(RoundedCornerShape(18.dp))
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                // logo painter sized to inside the box
                Image(
                    painter = logoPainter,
                    contentDescription = stringResource(R.string.app_name),
                    modifier = Modifier.fillMaxSize()
                        .padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.padding(top = MaterialTheme.spacing.sm))

            Text(
                text = heading,
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.padding(top =  MaterialTheme.spacing.sm))

            Text(
                text = subHeading,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}
