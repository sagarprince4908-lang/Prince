package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.SessionCompletionResult
import com.example.ui.theme.AccentViolet
import com.example.ui.theme.AccentVioletLight
import com.example.ui.theme.DarkCanvas
import com.example.ui.theme.GlassCardBorder
import com.example.ui.theme.GlassCardSurface
import com.example.ui.theme.TextDim
import com.example.ui.theme.TextFaded
import com.example.ui.theme.TextPure

@Composable
fun QuestCompleteDialog(
    result: SessionCompletionResult,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(
                            GlassCardSurface,
                            DarkCanvas
                        )
                    )
                )
                .border(1.dp, GlassCardBorder, RoundedCornerShape(24.dp))
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "✦",
                    color = AccentVioletLight,
                    fontSize = 28.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Session Completed",
                    color = TextPure,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.Center,
                    letterSpacing = (-0.3).sp
                )

                Text(
                    text = "Great focus and dedication.",
                    color = TextDim,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
                )

                // Reward Metrics Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(DarkCanvas.copy(alpha = 0.8f))
                        .border(1.dp, GlassCardBorder.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "XP EARNED", color = TextFaded, fontSize = 11.sp, fontWeight = FontWeight.Medium, letterSpacing = 1.2.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "+${result.earnedXp}",
                                color = AccentVioletLight,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(36.dp)
                                .background(GlassCardBorder.copy(alpha = 0.5f))
                        )

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "FOCUS CREDITS", color = TextFaded, fontSize = 11.sp, fontWeight = FontWeight.Medium, letterSpacing = 1.2.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "+${result.earnedCoins}",
                                color = TextPure,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                if (result.didLevelUp) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(AccentViolet.copy(alpha = 0.2f))
                            .border(1.dp, AccentViolet.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "✦ Reached Level ${result.newLevel}",
                            color = AccentVioletLight,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentViolet,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("quest_complete_claim_button")
                ) {
                    Text(
                        text = "Continue",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
