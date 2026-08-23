package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ScreenRotation
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AmbientSound
import com.example.model.Subject
import com.example.ui.components.FlipClockTile
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentElectricBlue
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.AccentPurpleLight
import com.example.ui.theme.DarkCanvas
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkCardSurface
import com.example.ui.theme.DarkCardSurfaceLight
import com.example.ui.theme.StatusLiveRed
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.TextBody
import com.example.ui.theme.TextDim
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPure
import com.example.viewmodel.AppTab
import com.example.viewmodel.StudyQuestUiState
import com.example.viewmodel.StudyQuestViewModel

@Composable
fun FlipClockScreen(
    uiState: StudyQuestUiState,
    viewModel: StudyQuestViewModel,
    modifier: Modifier = Modifier
) {
    if (uiState.isIdleClockMode) {
        IdleFlipClockView(uiState = uiState, viewModel = viewModel, modifier = modifier)
    } else {
        if (uiState.isLandscapeOrientation) {
            LandscapeFocusClockView(uiState = uiState, viewModel = viewModel, modifier = modifier)
        } else {
            PortraitFocusClockView(uiState = uiState, viewModel = viewModel, modifier = modifier)
        }
    }
}

/**
 * IDLE MODE: Minimalist desk flip clock showing current time, date, weather, and battery.
 */
@Composable
private fun IdleFlipClockView(
    uiState: StudyQuestUiState,
    viewModel: StudyQuestViewModel,
    modifier: Modifier = Modifier
) {
    val hourStr = String.format("%02d", uiState.idleCurrentHour)
    val minStr = String.format("%02d", uiState.idleCurrentMinute)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkCanvas)
            .padding(24.dp)
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    viewModel.setIdleClockMode(false)
                    viewModel.selectTab(AppTab.HOME)
                }
            ) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Exit Clock", tint = TextMuted)
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(DarkCardSurface)
                    .border(1.dp, DarkCardBorder, RoundedCornerShape(12.dp))
                    .clickable { viewModel.setIdleClockMode(false) }
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Start Focus Mode ✦",
                    color = AccentPurpleLight,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Center Big Clock & Side Info
        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Large Flip Clock Tiles
            FlipClockTile(
                digitString = hourStr,
                cardWidth = 110.dp,
                cardHeight = 136.dp,
                fontSize = 72.sp
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = ":",
                color = TextMuted,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.width(12.dp))

            FlipClockTile(
                digitString = minStr,
                cardWidth = 110.dp,
                cardHeight = 136.dp,
                fontSize = 72.sp
            )

            Spacer(modifier = Modifier.width(24.dp))

            // Right side metadata column
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = uiState.idleDayDateString,
                    color = TextPure,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🌙 8°C", color = TextMuted, fontSize = 12.sp)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🔋 ${uiState.batteryPercentage}%", color = TextMuted, fontSize = 12.sp)
                }
            }
        }

        // Bottom orientation toggle
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "IDLE CLOCK MODE",
                color = TextDim,
                fontSize = 11.sp,
                letterSpacing = 1.4.sp
            )
        }
    }
}

/**
 * PORTRAIT FOCUS CLOCK VIEW (Screen 4 in Infographic):
 * Stacked 00 / 24 / 38 tiles with side subject badge, Live indicator, and bottom controls.
 */
@Composable
private fun PortraitFocusClockView(
    uiState: StudyQuestUiState,
    viewModel: StudyQuestViewModel,
    modifier: Modifier = Modifier
) {
    val totalSecs = uiState.timerRemainingSeconds
    val hours = totalSecs / 3600
    val minutes = (totalSecs % 3600) / 60
    val seconds = totalSecs % 60

    val subjectName = if (uiState.selectedSubject == Subject.OTHER && uiState.customSubjectName.isNotBlank()) {
        uiState.customSubjectName
    } else {
        uiState.selectedSubject.displayName
    }

    val infiniteTransition = rememberInfiniteTransition(label = "live_pulse")
    val liveAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkCanvas)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top Action Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.selectTab(AppTab.HOME) },
                modifier = Modifier.testTag("flip_clock_close_button")
            ) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Flip Orientation Button
                IconButton(
                    onClick = { viewModel.toggleOrientation() },
                    modifier = Modifier.testTag("flip_orientation_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.ScreenRotation,
                        contentDescription = "Flip Orientation",
                        tint = AccentPurpleLight
                    )
                }

                // Ambient Audio Toggle Button
                IconButton(
                    onClick = {
                        val nextSound = if (uiState.ambientSound == AmbientSound.NONE) AmbientSound.RAIN else AmbientSound.NONE
                        viewModel.setAmbientSound(nextSound)
                    },
                    modifier = Modifier.testTag("ambient_sound_toggle_button")
                ) {
                    Icon(
                        imageVector = if (uiState.ambientSound != AmbientSound.NONE) Icons.Default.VolumeUp else Icons.Default.VolumeMute,
                        contentDescription = "Audio Soundscape",
                        tint = if (uiState.ambientSound != AmbientSound.NONE) AccentPurpleLight else TextMuted
                    )
                }
            }
        }

        // Center Area: Vertical Stacked Flip-Clock Numbers + Side Details (As Screen 4)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Stacked Flip Tiles: 00 / 24 / 38
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FlipClockTile(
                    digitString = String.format("%02d", hours),
                    cardWidth = 92.dp,
                    cardHeight = 100.dp,
                    fontSize = 58.sp
                )

                FlipClockTile(
                    digitString = String.format("%02d", minutes),
                    cardWidth = 92.dp,
                    cardHeight = 100.dp,
                    fontSize = 58.sp
                )

                FlipClockTile(
                    digitString = String.format("%02d", seconds),
                    cardWidth = 92.dp,
                    cardHeight = 100.dp,
                    fontSize = 58.sp
                )
            }

            Spacer(modifier = Modifier.width(28.dp))

            // Right-Side Information Block
            Column(
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                // Subject Box
                Column {
                    Text(
                        text = "${uiState.selectedSubject.emoji} $subjectName",
                        color = TextPure,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = uiState.chapterOrObjective,
                        color = TextMuted,
                        fontSize = 12.sp
                    )
                }

                // Focus Mode Badge
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(AccentPurple)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Focus Mode",
                        color = AccentPurpleLight,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Live Indicator
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(StatusLiveRed.copy(alpha = liveAlpha))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Live",
                        color = StatusLiveRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Battery Level
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "🔋 ${uiState.batteryPercentage}%",
                        color = TextMuted,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Bottom Controls: Pause / Play and Stop / Finish
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Pause / Play Button
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(DarkCardSurface)
                    .border(1.dp, DarkCardBorder, CircleShape)
                    .clickable {
                        if (uiState.isTimerPaused) viewModel.resumeTimer() else viewModel.pauseTimer()
                    }
                    .testTag("focus_pause_resume_button"),
                contentAlignment = Alignment.Center
            ) {
                if (uiState.isTimerPaused) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Resume", tint = TextPure)
                } else {
                    Text(text = "❚❚", color = TextPure, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.width(28.dp))

            // Stop / Complete Button
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(DarkCardSurface)
                    .border(1.dp, DarkCardBorder, CircleShape)
                    .clickable { viewModel.stopAndCompleteSession() }
                    .testTag("focus_stop_button"),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(TextPure)
                )
            }
        }
    }
}

/**
 * LANDSCAPE FOCUS CLOCK VIEW (Top diagram in infographic):
 * Horizontal massive 00 HOURS / 24 MINUTES / 38 SECONDS with Right Info Panel.
 */
@Composable
private fun LandscapeFocusClockView(
    uiState: StudyQuestUiState,
    viewModel: StudyQuestViewModel,
    modifier: Modifier = Modifier
) {
    val totalSecs = uiState.timerRemainingSeconds
    val hours = totalSecs / 3600
    val minutes = (totalSecs % 3600) / 60
    val seconds = totalSecs % 60

    val subjectName = if (uiState.selectedSubject == Subject.OTHER && uiState.customSubjectName.isNotBlank()) {
        uiState.customSubjectName
    } else {
        uiState.selectedSubject.displayName
    }

    val infiniteTransition = rememberInfiniteTransition(label = "live_pulse_land")
    val liveAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_land"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DarkCanvas)
            .padding(20.dp)
    ) {
        // Landscape Split: Left Big Flip Clock Units, Right Metadata & Controls
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Main Mechanical Flip Clock Horizontal Display (00 Hours / 24 Minutes / 38 Seconds)
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FlipClockTile(
                    digitString = String.format("%02d", hours),
                    label = "Hours",
                    cardWidth = 96.dp,
                    cardHeight = 118.dp,
                    fontSize = 62.sp
                )

                FlipClockTile(
                    digitString = String.format("%02d", minutes),
                    label = "Minutes",
                    cardWidth = 96.dp,
                    cardHeight = 118.dp,
                    fontSize = 62.sp
                )

                FlipClockTile(
                    digitString = String.format("%02d", seconds),
                    label = "Seconds",
                    cardWidth = 96.dp,
                    cardHeight = 118.dp,
                    fontSize = 62.sp
                )
            }

            // Right Panel: Details & Controls
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(180.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(DarkCardSurface.copy(alpha = 0.9f))
                    .border(1.dp, DarkCardBorder, RoundedCornerShape(20.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "${uiState.selectedSubject.emoji} $subjectName",
                        color = TextPure,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = uiState.chapterOrObjective,
                        color = TextMuted,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(AccentPurple)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Focus Mode", color = AccentPurpleLight, fontSize = 11.sp)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(StatusLiveRed.copy(alpha = liveAlpha))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Live", color = StatusLiveRed, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = "🔋 ${uiState.batteryPercentage}%", color = TextMuted, fontSize = 11.sp)
                }

                // Controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            if (uiState.isTimerPaused) viewModel.resumeTimer() else viewModel.pauseTimer()
                        }
                    ) {
                        if (uiState.isTimerPaused) {
                            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Resume", tint = TextPure)
                        } else {
                            Text(text = "❚❚", color = TextPure, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    IconButton(onClick = { viewModel.stopAndCompleteSession() }) {
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(TextPure)
                        )
                    }

                    IconButton(onClick = { viewModel.toggleOrientation() }) {
                        Icon(imageVector = Icons.Default.ScreenRotation, contentDescription = "Portrait", tint = AccentPurpleLight)
                    }
                }
            }
        }
    }
}
