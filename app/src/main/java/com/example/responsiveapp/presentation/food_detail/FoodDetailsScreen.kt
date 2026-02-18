package com.example.responsiveapp.presentation.food_detail

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.BookmarkAdded
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.responsiveapp.domain.model.NutritionInfo
import com.example.responsiveapp.domain.model.Serving
import com.example.responsiveapp.presentation.ui.theme.DeviceConfiguration
import com.example.responsiveapp.presentation.ui.theme.deviceConfiguration
import com.example.responsiveapp.presentation.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodDetailsScreen(
    userId: String,
    date: Long,
    onBackClick: () -> Unit,
    onLogSuccess: () -> Unit,
    viewModel: FoodDetailsViewModel = hiltViewModel()
){

}

// ─── Sub-Components ───────────────────────────────────────────────────────────

@Composable
private fun FoodNameHeader(
    name: String,
    brand: String?,
    isFavorite: Boolean,
    onFavoriteToggle: () -> Unit,
    deviceConfig: DeviceConfiguration
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = when (deviceConfig) {
                        DeviceConfiguration.DESKTOP -> 36.sp
                        DeviceConfiguration.TABLET -> 32.sp
                        DeviceConfiguration.MOBILE -> 28.sp
                    }
                ),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            if (!brand.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = brand,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        IconButton(
            onClick = onFavoriteToggle,
            modifier = Modifier.size(
                when (deviceConfig) {
                    DeviceConfiguration.DESKTOP -> 52.dp
                    DeviceConfiguration.TABLET -> 48.dp
                    DeviceConfiguration.MOBILE -> 44.dp
                }
            )
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Default.BookmarkAdded else Icons.Default.BookmarkBorder,
                contentDescription = if (isFavorite) "Remove bookmark" else "Bookmark",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(
                    when (deviceConfig) {
                        DeviceConfiguration.DESKTOP -> 32.dp
                        DeviceConfiguration.TABLET -> 30.dp
                        DeviceConfiguration.MOBILE -> 28.dp
                    }
                )
            )
        }
    }
}

@Composable
private fun ServingAmountControl(
    quantity: Float,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    deviceConfig: DeviceConfiguration
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Serving Amount",
            style = when (deviceConfig) {
                DeviceConfiguration.DESKTOP -> MaterialTheme.typography.titleLarge
                DeviceConfiguration.TABLET -> MaterialTheme.typography.titleMedium
                DeviceConfiguration.MOBILE -> MaterialTheme.typography.titleMedium
            },
            color = MaterialTheme.colorScheme.onSurface
        )

        Surface(
            shape = RoundedCornerShape(32.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
            border = ButtonDefaults.outlinedButtonBorder,
            modifier = Modifier.animateContentSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(
                    horizontal = when (deviceConfig) {
                        DeviceConfiguration.DESKTOP -> MaterialTheme.spacing.sm
                        DeviceConfiguration.TABLET -> 6.dp
                        DeviceConfiguration.MOBILE -> 4.dp
                    },
                    vertical = 4.dp
                )
            ) {
                val controlBtnSize = when (deviceConfig) {
                    DeviceConfiguration.DESKTOP -> 40.dp
                    DeviceConfiguration.TABLET -> 36.dp
                    DeviceConfiguration.MOBILE -> 32.dp
                }
                val controlIconSize = when (deviceConfig) {
                    DeviceConfiguration.DESKTOP -> 22.dp
                    DeviceConfiguration.TABLET -> 20.dp
                    DeviceConfiguration.MOBILE -> 18.dp
                }

                IconButton(
                    onClick = onDecrease,
                    modifier = Modifier.size(controlBtnSize)
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Decrease",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(controlIconSize)
                    )
                }

                Text(
                    text = formatQuantity(quantity),
                    style = when (deviceConfig) {
                        DeviceConfiguration.DESKTOP -> MaterialTheme.typography.headlineMedium
                        DeviceConfiguration.TABLET -> MaterialTheme.typography.titleLarge
                        DeviceConfiguration.MOBILE -> MaterialTheme.typography.titleLarge
                    }.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = MaterialTheme.spacing.md)
                )

                IconButton(
                    onClick = onIncrease,
                    modifier = Modifier.size(controlBtnSize)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Increase",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(controlIconSize)
                    )
                }
            }
        }
    }
}

@Composable
private fun CaloriesCard(
    calories: Float,
    deviceConfig: DeviceConfiguration
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(
            when (deviceConfig) {
                DeviceConfiguration.DESKTOP -> 24.dp
                DeviceConfiguration.TABLET -> 22.dp
                DeviceConfiguration.MOBILE -> 20.dp
            }
        ),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    when (deviceConfig) {
                        DeviceConfiguration.DESKTOP -> MaterialTheme.spacing.lg
                        DeviceConfiguration.TABLET -> MaterialTheme.spacing.md
                        DeviceConfiguration.MOBILE -> MaterialTheme.spacing.md
                    }
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val iconBgSize = when (deviceConfig) {
                DeviceConfiguration.DESKTOP -> 64.dp
                DeviceConfiguration.TABLET -> 60.dp
                DeviceConfiguration.MOBILE -> 56.dp
            }
            val fireIconSize = when (deviceConfig) {
                DeviceConfiguration.DESKTOP -> 34.dp
                DeviceConfiguration.TABLET -> 32.dp
                DeviceConfiguration.MOBILE -> 28.dp
            }

            Box(
                modifier = Modifier
                    .size(iconBgSize)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocalFireDepartment,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(fireIconSize)
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = MaterialTheme.spacing.md)
            ) {
                Text(
                    text = "Calories",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = calories.toInt().toString(),
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = when (deviceConfig) {
                            DeviceConfiguration.DESKTOP -> 56.sp
                            DeviceConfiguration.TABLET -> 48.sp
                            DeviceConfiguration.MOBILE -> 42.sp
                        }
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit calories",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(
                    when (deviceConfig) {
                        DeviceConfiguration.DESKTOP -> 26.dp
                        DeviceConfiguration.TABLET -> 24.dp
                        DeviceConfiguration.MOBILE -> 22.dp
                    }
                )
            )
        }
    }
}

@Composable
private fun MacrosRow(
    nutrition: NutritionInfo,
    deviceConfig: DeviceConfiguration
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(
            when (deviceConfig) {
                DeviceConfiguration.DESKTOP -> MaterialTheme.spacing.md
                DeviceConfiguration.TABLET -> MaterialTheme.spacing.sm + 4.dp
                DeviceConfiguration.MOBILE -> MaterialTheme.spacing.sm + 4.dp
            }
        )
    ) {
        MacroCard(
            icon = Icons.Default.Restaurant,
            label = "Protein",
            value = "${formatNutrient(nutrition.protein)}g",
            iconColor = Color(0xFFFF6B6B),
            modifier = Modifier.weight(1f),
            deviceConfig = deviceConfig
        )
        MacroCard(
            icon = Icons.Default.Grain,
            label = "Carbs",
            value = "${formatNutrient(nutrition.carbs)}g",
            iconColor = Color(0xFFFF9F43),
            modifier = Modifier.weight(1f),
            deviceConfig = deviceConfig
        )
        MacroCard(
            icon = Icons.Default.WaterDrop,
            label = "Fats",
            value = "${formatNutrient(nutrition.fat)}g",
            iconColor = Color(0xFF4B7BEC),
            modifier = Modifier.weight(1f),
            deviceConfig = deviceConfig
        )
    }
}

@Composable
private fun MacroCard(
    icon: ImageVector,
    label: String,
    value: String,
    iconColor: Color,
    modifier: Modifier = Modifier,
    deviceConfig: DeviceConfiguration
) {
    val cardShape = when (deviceConfig) {
        DeviceConfiguration.DESKTOP -> 20.dp
        DeviceConfiguration.TABLET -> 18.dp
        DeviceConfiguration.MOBILE -> 16.dp
    }
    val iconBgSize = when (deviceConfig) {
        DeviceConfiguration.DESKTOP -> 48.dp
        DeviceConfiguration.TABLET -> 44.dp
        DeviceConfiguration.MOBILE -> 40.dp
    }
    val iconSize = when (deviceConfig) {
        DeviceConfiguration.DESKTOP -> 26.dp
        DeviceConfiguration.TABLET -> 24.dp
        DeviceConfiguration.MOBILE -> 22.dp
    }
    val valueFontSize = when (deviceConfig) {
        DeviceConfiguration.DESKTOP -> MaterialTheme.typography.headlineMedium
        DeviceConfiguration.TABLET -> MaterialTheme.typography.headlineSmall
        DeviceConfiguration.MOBILE -> MaterialTheme.typography.headlineSmall
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(cardShape),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    when (deviceConfig) {
                        DeviceConfiguration.DESKTOP -> MaterialTheme.spacing.md
                        DeviceConfiguration.TABLET -> 14.dp
                        DeviceConfiguration.MOBILE -> MaterialTheme.spacing.sm + 4.dp
                    }
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm)
        ) {
            Box(
                modifier = Modifier
                    .size(iconBgSize)
                    .clip(CircleShape)
                    .background(iconColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(iconSize)
                )
            }

            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = value,
                style = valueFontSize.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun NutritionFactRow(
    label: String,
    value: String,
    deviceConfig: DeviceConfiguration
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(
            when (deviceConfig) {
                DeviceConfiguration.DESKTOP -> 20.dp
                DeviceConfiguration.TABLET -> 18.dp
                DeviceConfiguration.MOBILE -> 16.dp
            }
        ),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = when (deviceConfig) {
                        DeviceConfiguration.DESKTOP -> MaterialTheme.spacing.lg
                        DeviceConfiguration.TABLET -> MaterialTheme.spacing.md
                        DeviceConfiguration.MOBILE -> MaterialTheme.spacing.md
                    },
                    vertical = when (deviceConfig) {
                        DeviceConfiguration.DESKTOP -> MaterialTheme.spacing.md
                        DeviceConfiguration.TABLET -> 14.dp
                        DeviceConfiguration.MOBILE -> MaterialTheme.spacing.sm + 4.dp
                    }
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = when (deviceConfig) {
                    DeviceConfiguration.DESKTOP -> MaterialTheme.typography.bodyLarge
                    DeviceConfiguration.TABLET -> MaterialTheme.typography.bodyLarge
                    DeviceConfiguration.MOBILE -> MaterialTheme.typography.bodyMedium
                },
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = value,
                style = when (deviceConfig) {
                    DeviceConfiguration.DESKTOP -> MaterialTheme.typography.titleMedium
                    DeviceConfiguration.TABLET -> MaterialTheme.typography.titleMedium
                    DeviceConfiguration.MOBILE -> MaterialTheme.typography.bodyLarge
                }.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

private fun formatQuantity(quantity: Float): String {
    return if (quantity == quantity.toInt().toFloat()) {
        quantity.toInt().toString()
    } else {
        String.format("%.1f", quantity)
    }
}

private fun formatNutrient(value: Float): String {
    return if (value == value.toInt().toFloat()) {
        value.toInt().toString()
    } else {
        String.format("%.1f", value)
    }
}