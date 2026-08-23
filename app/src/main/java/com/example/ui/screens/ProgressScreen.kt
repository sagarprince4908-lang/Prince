package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.TimeFilter
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentElectricBlue
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.AccentPurpleLight
import com.example.ui.theme.DarkCanvas
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkCardSurface
import com.example.ui.theme.DarkCardSurfaceLight
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.TextBody
import com.example.ui.theme.TextDim
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPure
import com.example.ui.theme.WarmGold
import com.example.viewmodel.AppTab
import com.example.viewmodel.StudyQuestUiState
import com.example.viewmodel.StudyQuestViewModel

@Composable
fun ProgressScreen(
    uiState: StudyQuestUiState,
    viewModel: StudyQuestViewModel,
    modifier: Modifier = Modifier
) {
    val profile = uiState.userProfile
    val totalXp = profile?.xp ?: 2840
    val totalSessions = profile?.totalSessionsCompleted ?: 18
    val bestStreak = profile?.bestStreakDays ?: 7
    val completedTasks = profile?.completedTasksCount ?: 12

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkCanvas)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item { Spacer(modifier = Modifier.height(4.dp)) }

        // Top Navigation Bar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.selectTab(AppTab.HOME) }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back to Home",
                        tint = TextPure
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Progress",
                    color = TextPure,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Time Filter Tabs: Daily | Weekly | Monthly | Yearly
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(DarkCardSurface)
                    .border(1.dp, DarkCardBorder, RoundedCornerShape(14.dp))
                    .padding(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TimeFilter.values().forEach { filter ->
                        val isSelected = uiState.progressTimeFilter == filter
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    if (isSelected) AccentPurple else Color.Transparent
                                )
                                .clickable { viewModel.setProgressTimeFilter(filter) }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = filter.title,
                                color = if (isSelected) Color.White else TextMuted,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }

        // Focus Statistics Hero Card: This Week -> 06h 42m Total Focus Time + Bar Chart
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                DarkCardSurfaceLight.copy(alpha = 0.9f),
                                DarkCardSurface.copy(alpha = 0.95f)
                            )
                        )
                    )
                    .border(1.dp, DarkCardBorder, RoundedCornerShape(22.dp))
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "This Week",
                                color = TextMuted,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "06h 42m",
                                color = TextPure,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = (-0.3).sp
                            )
                            Text(
                                text = "Total Focus Time",
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        }

                        Text(
                            text = "›",
                            color = TextMuted,
                            fontSize = 24.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    // 7-Day Animated Bar Chart (Mon, Tue, Wed, Thu, Fri, Sat, Sun)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        val weeklyData = listOf(
                            Pair("Mon", 0.45f), // 45 min
                            Pair("Tue", 0.65f), // 60 min
                            Pair("Wed", 0.35f), // 35 min
                            Pair("Thu", 0.95f), // 90 min (peak)
                            Pair("Fri", 0.70f), // 50 min
                            Pair("Sat", 0.40f), // 40 min
                            Pair("Sun", 0.75f)  // 45 min today
                        )

                        weeklyData.forEach { (day, fraction) ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Bottom,
                                modifier = Modifier.fillMaxHeight()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(16.dp)
                                        .fillMaxHeight(fraction = fraction)
                                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                                        .background(
                                            Brush.verticalGradient(
                                                listOf(AccentElectricBlue, AccentPurple)
                                            )
                                        )
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = day,
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // 4 Achievement Metric Cards in 2x2 Grid (As in Screen 5)
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ProgressMetricCard(
                        icon = "⭐",
                        label = "Total XP",
                        value = "2,840",
                        modifier = Modifier.weight(1f)
                    )
                    ProgressMetricCard(
                        icon = "📅",
                        label = "Sessions",
                        value = "$totalSessions",
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ProgressMetricCard(
                        icon = "🔥",
                        label = "Best Streak",
                        value = "$bestStreak Days",
                        modifier = Modifier.weight(1f)
                    )
                    ProgressMetricCard(
                        icon = "✅",
                        label = "Completed Tasks",
                        value = "$completedTasks Tasks",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@Composable
private fun ProgressMetricCard(
    icon: String,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(DarkCardSurface)
            .border(1.dp, DarkCardBorder, RoundedCornerShape(18.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(text = icon, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = label,
                color = TextMuted,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                color = TextPure,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
