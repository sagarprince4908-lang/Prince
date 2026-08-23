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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.data.local.entity.AchievementEntity
import com.example.model.AmbientSound
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
fun ProfileScreen(
    uiState: StudyQuestUiState,
    viewModel: StudyQuestViewModel,
    modifier: Modifier = Modifier
) {
    val profile = uiState.userProfile
    val levelInfo = uiState.levelInfo
    val username = profile?.username ?: "Kazu"

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkCanvas)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item { Spacer(modifier = Modifier.height(4.dp)) }

        // Top Title
        item {
            Text(
                text = "Profile & Settings",
                color = TextPure,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.3).sp
            )
        }

        // Profile Identity Card
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        // Monogram Avatar
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        listOf(AccentPurple.copy(alpha = 0.4f), Color(0xFF1E1B4B))
                                    )
                                )
                                .border(1.dp, AccentPurpleLight.copy(alpha = 0.6f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = (username.firstOrNull() ?: 'K').toString().uppercase(),
                                color = TextPure,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = username,
                                    color = TextPure,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                IconButton(
                                    onClick = { viewModel.openEditNameDialog() },
                                    modifier = Modifier
                                        .size(24.dp)
                                        .padding(start = 4.dp)
                                        .testTag("character_rename_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit Name",
                                        tint = TextMuted,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }

                            Text(
                                text = "Level 07 · Scholar 🎓",
                                color = AccentPurpleLight,
                                fontSize = 12.sp
                            )
                        }
                    }

                    // Total focus pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(DarkCanvas)
                            .border(1.dp, DarkCardBorder, RoundedCornerShape(12.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "${profile?.totalStudyMinutes ?: 402}m",
                            color = TextPure,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // Ambient Soundscapes Selector
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "FOCUS SOUNDSCAPES",
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.4.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val sounds = listOf(AmbientSound.NONE, AmbientSound.RAIN, AmbientSound.LOFI_PULSE, AmbientSound.FOREST)
                    sounds.forEach { sound ->
                        val isSelected = uiState.ambientSound == sound
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (isSelected) AccentPurple else DarkCardSurface
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) AccentPurpleLight else DarkCardBorder,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { viewModel.setAmbientSound(sound) }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = sound.emoji, fontSize = 16.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = sound.title,
                                    color = if (isSelected) Color.White else TextMuted,
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }
        }

        // Replay Onboarding
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(DarkCardSurface)
                    .border(1.dp, DarkCardBorder, RoundedCornerShape(16.dp))
                    .clickable { viewModel.selectTab(AppTab.ONBOARDING) }
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "✨", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "View App Intro & Concept",
                            color = TextPure,
                            fontSize = 14.sp
                        )
                    }

                    Text(text = "View →", color = AccentPurpleLight, fontSize = 12.sp)
                }
            }
        }

        // Achievements Section
        item {
            Text(
                text = "MILESTONES & ACHIEVEMENTS",
                color = TextMuted,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.4.sp
            )
        }

        items(uiState.achievements) { achievement ->
            val progressFraction = (achievement.currentProgress.toFloat() / achievement.maxProgress.toFloat()).coerceIn(0f, 1f)
            val isReadyToClaim = achievement.isUnlocked && !achievement.isClaimed

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(DarkCardSurface)
                    .border(
                        width = 1.dp,
                        color = when {
                            achievement.isClaimed -> DarkCardBorder
                            isReadyToClaim -> AccentPurple
                            else -> DarkCardBorder
                        },
                        shape = RoundedCornerShape(18.dp)
                    )
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(DarkCanvas)
                                    .border(1.dp, DarkCardBorder, RoundedCornerShape(10.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = achievement.emoji, fontSize = 18.sp)
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = achievement.title,
                                    color = TextPure,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = achievement.description,
                                    color = TextMuted,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        when {
                            achievement.isClaimed -> {
                                Text(text = "Completed ✓", color = AccentPurpleLight, fontSize = 11.sp)
                            }
                            isReadyToClaim -> {
                                Button(
                                    onClick = { viewModel.claimAchievement(achievement.id) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = AccentPurple,
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .height(32.dp)
                                        .testTag("claim_achievement_${achievement.id}")
                                ) {
                                    Text(text = "Claim", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            else -> {
                                Text(
                                    text = "${achievement.currentProgress}/${achievement.maxProgress}",
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Progress bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(DarkCanvas)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(fraction = progressFraction)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(2.dp))
                                .background(if (achievement.isUnlocked) AccentPurple else AccentPurple.copy(alpha = 0.4f))
                        )
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}
