package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.FocusMode
import com.example.model.Subject
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentElectricBlue
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.AccentPurpleLight
import com.example.ui.theme.DarkCanvas
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkCardSurface
import com.example.ui.theme.DarkCardSurfaceLight
import com.example.ui.theme.TextBody
import com.example.ui.theme.TextDim
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPure
import com.example.viewmodel.AppTab
import com.example.viewmodel.StudyQuestUiState
import com.example.viewmodel.StudyQuestViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StartTaskScreen(
    uiState: StudyQuestUiState,
    viewModel: StudyQuestViewModel,
    modifier: Modifier = Modifier
) {
    val durationMinutes = uiState.timerTotalSeconds / 60
    val presetDurations = listOf(15, 25, 45, 60, 90)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkCanvas)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item { Spacer(modifier = Modifier.height(4.dp)) }

        // Top Navigation Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.selectTab(AppTab.HOME) },
                    modifier = Modifier.testTag("start_task_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPure
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Start New Task",
                    color = TextPure,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Section: Choose Subject
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "CHOOSE SUBJECT",
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.4.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Subject.values().forEach { subject ->
                        val isSelected = uiState.selectedSubject == subject
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    if (isSelected) AccentPurple.copy(alpha = 0.22f) else DarkCardSurface
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) AccentPurple else DarkCardBorder,
                                    shape = RoundedCornerShape(14.dp)
                                )
                                .clickable { viewModel.setSubject(subject) }
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                                .testTag("subject_chip_${subject.name}")
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = subject.emoji, fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = subject.displayName,
                                    color = if (isSelected) TextPure else TextBody,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }

                if (uiState.selectedSubject == Subject.OTHER) {
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = uiState.customSubjectName,
                        onValueChange = { viewModel.setCustomSubjectName(it) },
                        placeholder = { Text("Enter subject title...", color = TextMuted) },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = TextPure,
                            unfocusedTextColor = TextPure,
                            focusedContainerColor = DarkCardSurface,
                            unfocusedContainerColor = DarkCardSurface,
                            focusedIndicatorColor = AccentPurple,
                            unfocusedIndicatorColor = DarkCardBorder
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("custom_subject_input")
                    )
                }
            }
        }

        // Section: Chapter / Topic
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "CHAPTER OR OBJECTIVE",
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.4.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = uiState.chapterOrObjective,
                    onValueChange = { viewModel.setChapterOrObjective(it) },
                    placeholder = { Text("e.g. Chapter 03 Calculus", color = TextMuted) },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = TextPure,
                        unfocusedTextColor = TextPure,
                        focusedContainerColor = DarkCardSurface,
                        unfocusedContainerColor = DarkCardSurface,
                        focusedIndicatorColor = AccentPurple,
                        unfocusedIndicatorColor = DarkCardBorder
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("task_chapter_input")
                )
            }
        }

        // Section: Set Focus Time
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "SET FOCUS TIME",
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.4.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    presetDurations.take(4).forEach { preset ->
                        val isSelected = durationMinutes == preset
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
                                .clickable { viewModel.setDurationPreset(preset) }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${preset} min",
                                color = if (isSelected) Color.White else TextBody,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    presetDurations.drop(4).forEach { preset ->
                        val isSelected = durationMinutes == preset
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
                                .clickable { viewModel.setDurationPreset(preset) }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${preset} min",
                                color = if (isSelected) Color.White else TextBody,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }

                    // Custom preset button
                    val isCustom = !presetDurations.contains(durationMinutes)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isCustom) AccentPurple else DarkCardSurface
                            )
                            .border(
                                width = 1.dp,
                                color = if (isCustom) AccentPurpleLight else DarkCardBorder,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { viewModel.openCustomDurationDialog() }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isCustom) "${durationMinutes} min" else "Custom",
                            color = if (isCustom) Color.White else TextBody,
                            fontSize = 12.sp,
                            fontWeight = if (isCustom) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }

        // Section: Focus Mode (Deep Focus vs Normal Mode)
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "FOCUS MODE",
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.4.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val isDeepFocus = uiState.focusMode == FocusMode.DEEP_FOCUS
                    val isNormal = uiState.focusMode == FocusMode.STANDARD

                    // Deep Focus Card
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                if (isDeepFocus) AccentPurple.copy(alpha = 0.25f) else DarkCardSurface
                            )
                            .border(
                                width = 1.dp,
                                color = if (isDeepFocus) AccentPurple else DarkCardBorder,
                                shape = RoundedCornerShape(14.dp)
                            )
                            .clickable { viewModel.setFocusMode(FocusMode.DEEP_FOCUS) }
                            .padding(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(androidx.compose.foundation.shape.CircleShape)
                                    .background(if (isDeepFocus) AccentPurple else TextDim)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Deep Focus",
                                    color = if (isDeepFocus) TextPure else TextBody,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "+25% XP bonus",
                                    color = AccentPurpleLight,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }

                    // Normal Mode Card
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                if (isNormal) AccentPurple.copy(alpha = 0.25f) else DarkCardSurface
                            )
                            .border(
                                width = 1.dp,
                                color = if (isNormal) AccentPurple else DarkCardBorder,
                                shape = RoundedCornerShape(14.dp)
                            )
                            .clickable { viewModel.setFocusMode(FocusMode.STANDARD) }
                            .padding(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(androidx.compose.foundation.shape.CircleShape)
                                    .background(if (isNormal) AccentPurple else TextDim)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Normal Mode",
                                    color = if (isNormal) TextPure else TextBody,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "Relaxed pacing",
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Large CTA: Start & Flip Button
        item {
            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = { viewModel.startTaskAndFlip() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .shadow(elevation = 16.dp, shape = RoundedCornerShape(16.dp), spotColor = AccentPurple.copy(alpha = 0.5f))
                    .background(
                        Brush.horizontalGradient(
                            listOf(AccentPurple, AccentElectricBlue)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .testTag("start_and_flip_button")
            ) {
                Text(
                    text = "Start & Flip",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.4.sp
                )
            }
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}
