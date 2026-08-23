package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkElegantColorScheme = darkColorScheme(
    primary = AccentViolet,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF2E1065),
    onPrimaryContainer = Color(0xFFEDE9FE),
    secondary = AccentVioletLight,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF1E1B4B),
    onSecondaryContainer = Color(0xFFE0E7FF),
    tertiary = WarmGold,
    onTertiary = Color.Black,
    background = DarkCanvas,
    onBackground = TextPure,
    surface = GlassCardSurface,
    onSurface = TextPure,
    surfaceVariant = GlassCardSurfaceLight,
    onSurfaceVariant = TextBody,
    outline = GlassCardBorder
)

@Composable
fun StudyQuestTheme(
    themeId: String = "theme_cyber_dark",
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkElegantColorScheme,
        typography = Typography,
        content = content
    )
}
