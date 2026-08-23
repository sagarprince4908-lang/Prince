package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.LevelInfo
import com.example.ui.theme.AccentViolet
import com.example.ui.theme.AccentVioletLight
import com.example.ui.theme.DarkCanvas
import com.example.ui.theme.GlassCardBorder
import com.example.ui.theme.GlassCardSurface
import com.example.ui.theme.TextDim
import com.example.ui.theme.TextFaded
import com.example.ui.theme.TextPure
import java.text.NumberFormat
import java.util.Locale

@Composable
fun XpProgressBar(
    levelInfo: LevelInfo,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = levelInfo.progressPercent,
        animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing),
        label = "xp_progress"
    )

    val xpNeeded = (levelInfo.xpForNextLevel - levelInfo.currentXp).coerceAtLeast(0)
    val formattedXpNeeded = NumberFormat.getNumberInstance(Locale.US).format(xpNeeded)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF222634).copy(alpha = 0.85f),
                        GlassCardSurface.copy(alpha = 0.95f)
                    )
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    listOf(
                        Color(0xFF475569).copy(alpha = 0.4f),
                        GlassCardBorder.copy(alpha = 0.3f)
                    )
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(18.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = String.format(Locale.US, "LEVEL %02d", levelInfo.level),
                    color = AccentVioletLight,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.8.sp
                )
                Text(
                    text = "${(animatedProgress * 100).toInt()}%",
                    color = TextPure,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Minimal sleek progress track
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(DarkCanvas.copy(alpha = 0.8f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = animatedProgress.coerceIn(0.02f, 1f))
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(3.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(AccentViolet, AccentVioletLight)
                            )
                        )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$formattedXpNeeded XP to your next milestone",
                    color = TextDim,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )

                Text(
                    text = levelInfo.title,
                    color = TextFaded,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}
