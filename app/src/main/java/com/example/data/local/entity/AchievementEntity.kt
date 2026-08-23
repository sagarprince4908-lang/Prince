package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "achievements")
data class AchievementEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val emoji: String,
    val category: String, // "STREAK", "TIME", "SESSIONS", "SPECIAL"
    val maxProgress: Int,
    val currentProgress: Int = 0,
    val isUnlocked: Boolean = false,
    val isClaimed: Boolean = false,
    val rewardXp: Int,
    val rewardCoins: Int,
    val rewardGems: Int = 0
)
