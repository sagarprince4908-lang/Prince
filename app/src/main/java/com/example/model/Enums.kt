package com.example.model

enum class Subject(val displayName: String, val emoji: String, val colorHex: Long) {
    MATH("Mathematics", "📖", 0xFF785CFF),
    PHYSICS("Physics", "⚛️", 0xFF5D7BFF),
    CHEMISTRY("Chemistry", "🧪", 0xFF38BDF8),
    ENGLISH("English", "📚", 0xFFF59E0B),
    ACCOUNTANCY("Accountancy", "📊", 0xFF10B981),
    ECONOMICS("Economics", "📈", 0xFFA855F7),
    OTHER("Other", "✏️", 0xFF64748B)
}

enum class FocusMode(val title: String, val subtitle: String, val icon: String, val xpMultiplier: Float) {
    DEEP_FOCUS("Deep Focus", "Stronger distraction-free experience · 1.25x XP", "🟣", 1.25f),
    STANDARD("Normal Mode", "Standard study pace with relaxed timer · 1.0x XP", "⏱️", 1.0f),
    POMODORO("Pomodoro", "25 min focus / 5 min rest · 1.1x XP", "🍅", 1.1f),
    ZEN_FLOW("Ambient Flow", "Soothing audio soundscapes · 1.15x XP", "🌌", 1.15f)
}

enum class AmbientSound(val title: String, val emoji: String, val frequency: Float) {
    NONE("Mute", "🔇", 0f),
    RAIN("Rainfall", "🌧️", 400f),
    CAMPFIRE("Campfire", "🔥", 300f),
    WHITE_NOISE("Cosmic Noise", "🌌", 600f),
    LOFI_PULSE("Binaural Beats", "🎧", 216f),
    FOREST("Deep Forest", "🍃", 520f)
}

enum class TimeFilter(val title: String) {
    DAILY("Daily"),
    WEEKLY("Weekly"),
    MONTHLY("Monthly"),
    YEARLY("Yearly")
}

data class LevelInfo(
    val level: Int,
    val title: String,
    val iconEmoji: String,
    val currentXp: Int,
    val xpForCurrentLevel: Int,
    val xpForNextLevel: Int,
    val progressPercent: Float,
    val characterEvolutionStage: String,
    val unlockedPerks: List<String>
)
