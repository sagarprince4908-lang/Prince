package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_quests")
data class DailyQuestEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val emoji: String,
    val targetType: String, // "STUDY_MINUTES", "SESSIONS_COUNT", "SUBJECT_VARIETY", "DEEP_FOCUS_COUNT"
    val targetValue: Int,
    val currentValue: Int = 0,
    val xpReward: Int,
    val coinReward: Int,
    val isCompleted: Boolean = false,
    val isClaimed: Boolean = false,
    val questDate: String // YYYY-MM-DD
)
