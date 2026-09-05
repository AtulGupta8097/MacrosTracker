package com.example.responsiveapp.presentation.settings.componet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.domain.model.ThemeMode
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun ThemeSection(
    selected: ThemeMode,
    onThemeSelected: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    SettingsSectionCard(modifier = modifier, label = "Appearance") {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.md),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm),
        ) {
            ThemeOption(
                label = "Light",
                selected = selected == ThemeMode.LIGHT,
                onClick = { onThemeSelected(ThemeMode.LIGHT) },
                modifier = Modifier.weight(1f),
            ) {
                ThemePreviewSwatch(
                    background = Color(0xFFFFFFFF),
                    bar = Color(0xFFF5F5F5),
                    dot = Color(0xFFE17E47),
                )
            }
            ThemeOption(
                label = "Dark",
                selected = selected == ThemeMode.DARK,
                onClick = { onThemeSelected(ThemeMode.DARK) },
                modifier = Modifier.weight(1f),
            ) {
                ThemePreviewSwatch(
                    background = Color(0xFF0F0F0F),
                    bar = Color(0xFF222223),
                    dot = Color(0xFFFF8A3D),
                )
            }
            ThemeOption(
                label = "System",
                selected = selected == ThemeMode.SYSTEM,
                onClick = { onThemeSelected(ThemeMode.SYSTEM) },
                modifier = Modifier.weight(1f),
            ) {
                ThemePreviewSwatchSplit()
            }
        }
    }
}

@Composable
private fun ThemeOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    preview: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.background)
            .border(
                BorderStroke(
                    width = 2.dp,
                    color = if (selected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
                    },
                ),
                RoundedCornerShape(16.dp),
            )
            .clickable(onClick = onClick)
            .padding(MaterialTheme.spacing.sm),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Box {
            preview()
            if (selected) {
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .align(Alignment.TopEnd)
                        .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(50))
                        .padding(2.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.height(10.dp),
                    )
                }
            }
        }

        Text(
            text = label,
            modifier = Modifier.padding(top = MaterialTheme.spacing.xs),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun ThemePreviewSwatch(
    background: Color,
    bar: Color,
    dot: Color,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(background),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(14.dp)
                .background(bar)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Box(
                modifier = Modifier
                    .height(8.dp)
                    .padding(0.dp)
                    .background(dot, RoundedCornerShape(50))
                    .padding(horizontal = 4.dp)
            )
        }
    }
}

@Composable
private fun ThemePreviewSwatchSplit() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(RoundedCornerShape(10.dp)),
    ) {
        Box(modifier = Modifier
            .weight(1f)
            .fillMaxWidth()) {
            ThemePreviewSwatch(
                background = Color(0xFFFFFFFF),
                bar = Color(0xFFF5F5F5),
                dot = Color(0xFFE17E47),
            )
        }
        Box(modifier = Modifier
            .weight(1f)
            .fillMaxWidth()) {
            ThemePreviewSwatch(
                background = Color(0xFF0F0F0F),
                bar = Color(0xFF222223),
                dot = Color(0xFFFF8A3D),
            )
        }
    }
}

@Preview
@Composable
private fun PrevThemeSection() {
    ThemeSection(selected = ThemeMode.LIGHT, onThemeSelected = {})
}
