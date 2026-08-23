package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import com.example.data.local.entity.DailyQuestEntity
import com.example.ui.theme.AccentViolet
import com.example.ui.theme.AccentVioletLight
import com.example.ui.theme.DarkCanvas
import com.example.ui.theme.GlassCardBorder
import com.example.ui.theme.GlassCardSurface
import com.example.ui.theme.TextBody
import com.example.ui.theme.TextDim
import com.example.ui.theme.TextFaded
import com.example.ui.theme.TextPure
import com.example.ui.theme.WarmGold

@Composable
fun QuestItemCard(
    quest: DailyQuestEntity,
    onClaimClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val progressFraction = (quest.currentValue.toFloat() / quest.targetValue.toFloat()).coerceIn(0f, 1f)
    val isReadyToClaim = quest.isCompleted && !quest.isClaimed

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        GlassCardSurface.copy(alpha = 0.85f),
                        Color(0xFF141622).copy(alpha = 0.95f)
                    )
                )
            )
            .border(
                width = 1.dp,
                color = when {
                    quest.isClaimed -> GlassCardBorder.copy(alpha = 0.35f)
                    isReadyToClaim -> AccentViolet.copy(alpha = 0.6f)
                    else -> GlassCardBorder.copy(alpha = 0.3f)
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
                            .background(DarkCanvas.copy(alpha = 0.7f))
                            .border(1.dp, GlassCardBorder.copy(alpha = 0.4f), RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = quest.emoji, fontSize = 18.sp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = quest.title,
                            color = TextPure,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = quest.description,
                            color = TextDim,
                            fontSize = 12.sp
                        )
                    }
                }

                // Action or Status
                when {
                    quest.isClaimed -> {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Completed",
                                tint = AccentVioletLight,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Completed", color = AccentVioletLight, fontSize = 11.sp)
                        }
                    }
                    isReadyToClaim -> {
                        Button(
                            onClick = { onClaimClick(quest.id) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AccentViolet,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .height(32.dp)
                                .testTag("claim_quest_${quest.id}")
                        ) {
                            Text(text = "Claim", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                    else -> {
                        Text(
                            text = "${quest.currentValue}/${quest.targetValue}",
                            color = TextFaded,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Minimal Progress bar
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
                        .background(
                            if (isReadyToClaim || quest.isClaimed) AccentViolet else AccentViolet.copy(alpha = 0.4f)
                        )
                )
            }
        }
    }
}
