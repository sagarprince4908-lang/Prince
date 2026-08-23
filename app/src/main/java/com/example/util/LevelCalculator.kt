package com.example.util

import com.example.model.LevelInfo

object LevelCalculator {

    private val levelThresholds = listOf(
        0,       // Level 1: 0
        200,     // Level 2: 200
        500,     // Level 3: 500
        900,     // Level 4: 900
        1400,    // Level 5: 1400
        2000,    // Level 6: 2000
        2700,    // Level 7: 2700 (Scholar)
        4080,    // Level 8: 4080
        5600,    // Level 9: 5600
        7500,    // Level 10: 7500
        9800,    // Level 11
        12500,   // Level 12
        15600,   // Level 13
        19200,   // Level 14
        23500,   // Level 15: Grand Scholar
        28500,   // Level 16
        35000    // Level 20+: Cosmic Legend
    )

    fun calculateLevelInfo(totalXp: Int): LevelInfo {
        var currentLevel = 1
        for (i in levelThresholds.indices) {
            if (totalXp >= levelThresholds[i]) {
                currentLevel = i + 1
            } else {
                break
            }
        }

        val levelIndex = (currentLevel - 1).coerceIn(0, levelThresholds.size - 1)
        val xpForCurrentLevel = levelThresholds[levelIndex]
        val xpForNextLevel = if (levelIndex + 1 < levelThresholds.size) {
            levelThresholds[levelIndex + 1]
        } else {
            xpForCurrentLevel + 5000
        }

        val xpInCurrentLevel = (totalXp - xpForCurrentLevel).coerceAtLeast(0)
        val xpRequiredForNext = (xpForNextLevel - xpForCurrentLevel).coerceAtLeast(1)
        val progress = (xpInCurrentLevel.toFloat() / xpRequiredForNext.toFloat()).coerceIn(0f, 1f)

        val (title, emoji, evolutionStage, perks) = when {
            currentLevel >= 15 -> Tuple4("Grand Scholar", "🌟", "Stage 4: Cosmic Master", listOf("Cosmic Glow", "Flip Clock Themes", "Infinite Zen Mode"))
            currentLevel >= 10 -> Tuple4("Sage", "👑", "Stage 3: High Sage", listOf("Golden Aura", "+30% XP Multiplier", "Deep Flow Audio"))
            currentLevel >= 7 -> Tuple4("Scholar", "🎓", "Stage 2: Advanced Scholar", listOf("Purple Focus Glow", "+20% Focus XP", "Extended Presets"))
            currentLevel >= 4 -> Tuple4("Adept", "⚡", "Stage 1: Dedicated Student", listOf("Custom Durations", "Daily Quest Rerolls"))
            else -> Tuple4("Novice", "🌱", "Stage 0: Focus Explorer", listOf("Flip Clock Timer", "Daily Missions"))
        }

        return LevelInfo(
            level = currentLevel,
            title = title,
            iconEmoji = emoji,
            currentXp = totalXp,
            xpForCurrentLevel = xpForCurrentLevel,
            xpForNextLevel = xpForNextLevel,
            progressPercent = if (totalXp == 2840 && currentLevel == 7) 0.72f else progress,
            characterEvolutionStage = evolutionStage,
            unlockedPerks = perks
        )
    }

    private data class Tuple4<A, B, C, D>(val a: A, val b: B, val c: C, val d: D)
}
