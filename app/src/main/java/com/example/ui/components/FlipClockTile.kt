package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkCanvas
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkCardSurface
import com.example.ui.theme.DarkCardSurfaceLight
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPure

/**
 * Mechanical Flip-Clock Tile Component
 * Simulates a realistic physical flip-clock plate with split halves and 3D flip animation.
 */
@Composable
fun FlipClockTile(
    digitString: String,
    label: String? = null,
    modifier: Modifier = Modifier,
    cardWidth: Dp = 88.dp,
    cardHeight: Dp = 104.dp,
    fontSize: TextUnit = 54.sp
) {
    var displayedDigit by remember { mutableStateOf(digitString) }
    var previousDigit by remember { mutableStateOf(digitString) }
    val flipAnimation = remember { Animatable(0f) }

    LaunchedEffect(digitString) {
        if (digitString != displayedDigit) {
            previousDigit = displayedDigit
            displayedDigit = digitString
            flipAnimation.snapTo(0f)
            flipAnimation.animateTo(
                targetValue = 180f,
                animationSpec = tween(durationMillis = 360, easing = FastOutSlowInEasing)
            )
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        // The Mechanical Flip Card
        Box(
            modifier = Modifier
                .width(cardWidth)
                .height(cardHeight)
                .shadow(elevation = 12.dp, shape = RoundedCornerShape(16.dp), spotColor = Color.Black.copy(alpha = 0.6f))
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF1E212A),
                            DarkCardSurface,
                            Color(0xFF15171F)
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(
                        listOf(
                            Color(0xFF383F4E).copy(alpha = 0.6f),
                            DarkCardBorder
                        )
                    ),
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            // Large Digit Text
            Text(
                text = displayedDigit,
                color = TextPure,
                fontSize = fontSize,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .graphicsLayer {
                        if (flipAnimation.value in 1f..179f) {
                            rotationX = if (flipAnimation.value < 90f) -flipAnimation.value * 0.15f else (180f - flipAnimation.value) * 0.15f
                        }
                    }
            )

            // Horizontal Mechanical Center Split Seam
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xFF090B0F).copy(alpha = 0.9f),
                                Color(0xFF282C38).copy(alpha = 0.5f)
                            )
                        )
                    )
            )

            // Left Side Mechanical Notch
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(width = 4.dp, height = 8.dp)
                    .clip(RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp))
                    .background(Color(0xFF0B1016))
            )

            // Right Side Mechanical Notch
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(width = 4.dp, height = 8.dp)
                    .clip(RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp))
                    .background(Color(0xFF0B1016))
            )
        }

        if (label != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label.uppercase(),
                color = TextMuted,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.6.sp
            )
        }
    }
}

/**
 * Double-tile flip clock component (e.g. for "23" or "24")
 */
@Composable
fun FlipClockUnit(
    value: Int,
    label: String,
    modifier: Modifier = Modifier,
    cardWidth: Dp = 96.dp,
    cardHeight: Dp = 116.dp,
    fontSize: TextUnit = 60.sp
) {
    val formatted = String.format("%02d", value)
    FlipClockTile(
        digitString = formatted,
        label = label,
        modifier = modifier,
        cardWidth = cardWidth,
        cardHeight = cardHeight,
        fontSize = fontSize
    )
}
