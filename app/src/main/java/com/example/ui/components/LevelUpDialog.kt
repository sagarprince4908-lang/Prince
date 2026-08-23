package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.example.model.LevelInfo
import com.example.ui.theme.AccentViolet
import com.example.ui.theme.AccentVioletLight
import com.example.ui.theme.DarkCanvas
import com.example.ui.theme.GlassCardBorder
import com.example.ui.theme.GlassCardSurface
import com.example.ui.theme.TextDim
import com.example.ui.theme.TextFaded
import com.example.ui.theme.TextPure
import java.util.Locale

@Composable
fun LevelUpDialog(
    levelInfo: LevelInfo,
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
                    text = String.format(Locale.US, "Level %02d Milestone", levelInfo.level),
                    color = TextPure,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.Center,
                    letterSpacing = (-0.3).sp
                )

                Text(
                    text = levelInfo.title,
                    color = AccentVioletLight,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(top = 2.dp, bottom = 18.dp)
                )

                // Perks Unlocked Box
                if (levelInfo.unlockedPerks.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(DarkCanvas.copy(alpha = 0.8f))
                            .border(1.dp, GlassCardBorder.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                            .padding(14.dp)
                    ) {
                        Text(
                            text = "UNLOCKED CAPABILITIES",
                            color = TextFaded,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.2.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        levelInfo.unlockedPerks.forEach { perk ->
                            Text(
                                text = "· $perk",
                                color = TextDim,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                        }
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
                        .testTag("level_up_claim_button")
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
