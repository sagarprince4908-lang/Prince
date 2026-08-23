package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// 🌑 Core Dark Aesthetic Palette (#0B1016, #1A1C23, #262A33)
val DarkCanvas = Color(0xFF0B1016)          // Very dark navy/black background
val DarkCanvasVariant = Color(0xFF0F141D)   // Subtle auxiliary background
val DarkCardSurface = Color(0xFF1A1C23)     // Dark blue-gray glassmorphism surface
val DarkCardSurfaceLight = Color(0xFF222631)// Lighter surface
val DarkCardBorder = Color(0xFF262A33)      // Subtle refined border
val DarkCardBorderHighlight = Color(0xFF383F4E)

// ✨ Signature Accent Colors
val AccentPurple = Color(0xFF785CFF)        // Signature Purple/Violet
val AccentPurpleLight = Color(0xFF9A85FF)   // Purple highlight
val AccentElectricBlue = Color(0xFF5D7BFF)  // Electric Blue
val AccentCyan = Color(0xFF38BDF8)          // Accent Cyan
val WarmGold = Color(0xFFF59E0B)            // Streak / Milestone Gold
val StatusLiveRed = Color(0xFFFF4545)       // Status: 🔴 Live
val StatusSuccess = Color(0xFF10B981)       // Status: 🟢 Success

// 📖 Typography Colors
val TextPure = Color(0xFFFFFFFF)            // Pure White
val TextBody = Color(0xFFE2E8F0)            // Clean light slate
val TextMuted = Color(0xFF9FA6B2)           // Muted Gray (#9FA6B2)
val TextDim = Color(0xFF6B7280)             // Dim slate
val TextLocked = Color(0xFF4B5563)          // Dark locked state

// Backwards compatibility aliases
val GlassCardSurface = DarkCardSurface
val GlassCardSurfaceLight = DarkCardSurfaceLight
val GlassCardBorder = DarkCardBorder
val GlassCardBorderHighlight = DarkCardBorderHighlight
val AccentViolet = AccentPurple
val AccentVioletLight = AccentPurpleLight
val AccentVioletGlow = Color(0x40785CFF)
val AccentBlue = AccentElectricBlue
val SoftCyan = AccentCyan
val SuccessMuted = StatusSuccess
val TextFaded = TextMuted
