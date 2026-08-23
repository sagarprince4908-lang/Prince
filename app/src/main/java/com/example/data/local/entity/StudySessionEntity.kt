package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "study_sessions")
data class StudySessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val subject: String,
    val durationMinutes: Int,
    val xpEarned: Int,
    val coinsEarned: Int,
    val focusMode: String,
    val timestamp: Long = System.currentTimeMillis(),
    val notes: String = "",
    val completed: Boolean = true
)
