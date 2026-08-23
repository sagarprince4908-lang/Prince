package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val username: String = "Kazu",
    val title: String = "Scholar 🎓",
    val level: Int = 7,
    val xp: Int = 2840,
    val coins: Int = 320,
    val gems: Int = 25,
    val streakDays: Int = 7,
    val bestStreakDays: Int = 7,
    val lastStudyDateMillis: Long = 0L,
    val totalStudyMinutes: Int = 90,
    val totalSessionsCompleted: Int = 18,
    val completedTasksCount: Int = 12,
    val streakFreezeCount: Int = 1,
    val doubleXpPotionsCount: Int = 1,
    val isDoubleXpActive: Boolean = false,
    val selectedAvatar: String = "hero_default",
    val selectedTheme: String = "theme_cyber_dark",
    val equippedPet: String = "pet_owl",
    val intelligenceStat: Int = 14,
    val focusStat: Int = 18,
    val staminaStat: Int = 12,
    val characterEvolutionStage: Int = 2,
    val isOnboardingCompleted: Boolean = true
)
