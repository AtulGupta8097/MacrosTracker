package com.example.responsiveapp.presentation.main_screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.responsiveapp.presentation.main_screen.AddFoodOption


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFoodBottomSheet(
    onDismiss: () -> Unit,
    onOptionSelected: (String) -> Unit,
) {
    val sheetState =
        rememberModalBottomSheetState(
            skipPartiallyExpanded = true,
        )

    val options =
        listOf(
            AddFoodOption(
                title = "Food Database",
                subtitle = "Search thousands of foods",
                icon = Icons.Outlined.Book,
                iconColor = Color(0xFF10B981),
                backgroundColor = Color(0xFF10B981).copy(alpha = 0.1f),
            ),
            AddFoodOption(
                title = "Scan Barcode",
                subtitle = "Scan packaged food instantly",
                icon = Icons.Outlined.QrCodeScanner,
                iconColor = Color(0xFF0EA5E9),
                backgroundColor = Color(0xFF0EA5E9).copy(alpha = 0.1f),
            ),
            AddFoodOption(
                title = "Scan Food",
                subtitle = "Identify food using camera",
                icon = Icons.Outlined.CameraAlt,
                iconColor = Color(0xFF8B5CF6),
                backgroundColor = Color(0xFF8B5CF6).copy(alpha = 0.1f),
            ),
            AddFoodOption(
                title = "Saved Foods",
                subtitle = "Quickly add your favorites",
                icon = Icons.Outlined.Star,
                iconColor = Color(0xFFF59E0B),
                backgroundColor = Color(0xFFF59E0B).copy(alpha = 0.1f),
            ),
        )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        dragHandle = {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 14.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier =
                        Modifier
                            .width(40.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)),
                )
            }
        },
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 40.dp),
        ) {
            Text(
                text = "Add Food",
                style =
                    MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                    ),
                color = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Choose how you'd like to log your meal",
                style =
                    MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 15.sp,
                    ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(28.dp))

            options.forEach { option ->
                AddFoodOptionItem(
                    option = option,
                    onClick = {
                        onOptionSelected(option.title)
                        onDismiss()
                    },
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun AddFoodOptionItem(
    option: AddFoodOption,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier =
            Modifier
                .fillMaxWidth()
                .height(72.dp),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier =
                    Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(option.backgroundColor),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = option.icon,
                    contentDescription = option.title,
                    tint = option.iconColor,
                    modifier = Modifier.size(24.dp),
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = option.title,
                    style =
                        MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                        ),
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = option.subtitle,
                    style =
                        MaterialTheme.typography.bodySmall.copy(
                            fontSize = 13.sp,
                        ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                )
            }
        }
    }
}
