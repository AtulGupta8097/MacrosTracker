package com.example.responsiveapp.presentation.home.componet.skeleton

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun NutritionSectionSkeleton(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.spacing.md),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.md),
    ) {

        // Calories card silhouette: title bar + ring + two text lines
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
        ) {
            Column(
                modifier = Modifier.padding(MaterialTheme.spacing.md),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm),
            ) {

                SkeletonBox(
                    modifier = Modifier
                        .width(120.dp)
                        .height(18.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.md),
                ) {

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm),
                    ) {

                        SkeletonBox(
                            modifier = Modifier
                                .width(90.dp)
                                .height(28.dp)
                        )

                        SkeletonBox(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp),
                            shape = RoundedCornerShape(50),
                        )

                        SkeletonBox(
                            modifier = Modifier
                                .width(100.dp)
                                .height(30.dp),
                            shape = RoundedCornerShape(50),
                        )
                    }

                    SkeletonBox(
                        modifier = Modifier
                            .weight(0.6f)
                            .aspectRatio(1f),
                        shape = CircleShape,
                    )
                }
            }
        }

        // Macro grid silhouette: 2x2 cards
        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm),
        ) {
            repeat(2) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm),
                ) {
                    repeat(2) {
                        MacroCardSkeleton(
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MacroCardSkeleton(
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        Column(
            modifier = Modifier.padding(MaterialTheme.spacing.md),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm),
        ) {

            Row(
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm),
            ) {
                SkeletonBox(
                    modifier = Modifier.size(34.dp),
                    shape = CircleShape,
                )
                SkeletonBox(
                    modifier = Modifier
                        .width(64.dp)
                        .height(18.dp)
                )
            }

            SkeletonBox(
                modifier = Modifier
                    .width(70.dp)
                    .height(22.dp)
            )

            SkeletonBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp),
                shape = RoundedCornerShape(50),
            )
        }
    }
}

@Composable
fun HealthMetricsSectionSkeleton(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.spacing.md),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm),
    ) {

        SkeletonBox(
            modifier = Modifier
                .width(130.dp)
                .height(18.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.md),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.md),
            ) {
                SkeletonBox(
                    modifier = Modifier.size(64.dp),
                    shape = CircleShape,
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm),
                ) {
                    SkeletonBox(
                        modifier = Modifier
                            .width(60.dp)
                            .height(26.dp)
                    )
                    SkeletonBox(
                        modifier = Modifier
                            .width(90.dp)
                            .height(14.dp)
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm),
        ) {
            repeat(2) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(96.dp),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
                ) {
                    Column(
                        modifier = Modifier.padding(MaterialTheme.spacing.md),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm),
                    ) {
                        SkeletonBox(
                            modifier = Modifier
                                .width(50.dp)
                                .height(14.dp)
                        )
                        SkeletonBox(
                            modifier = Modifier
                                .width(70.dp)
                                .height(22.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FoodLogsSectionSkeleton(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.spacing.md),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm),
    ) {

        SkeletonBox(
            modifier = Modifier
                .width(150.dp)
                .height(18.dp)
        )

        repeat(2) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
            ) {
                Column(
                    modifier = Modifier.padding(MaterialTheme.spacing.md),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm),
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        SkeletonBox(
                            modifier = Modifier
                                .width(140.dp)
                                .height(18.dp)
                        )
                        SkeletonBox(
                            modifier = Modifier
                                .width(50.dp)
                                .height(14.dp)
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.lg),
                    ) {
                        repeat(3) {
                            SkeletonBox(
                                modifier = Modifier
                                    .width(40.dp)
                                    .height(14.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}