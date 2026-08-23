package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.AchievementDao
import com.example.data.local.dao.DailyQuestDao
import com.example.data.local.dao.ShopItemDao
import com.example.data.local.dao.StudySessionDao
import com.example.data.local.dao.UserProfileDao
import com.example.data.local.entity.AchievementEntity
import com.example.data.local.entity.DailyQuestEntity
import com.example.data.local.entity.ShopItemEntity
import com.example.data.local.entity.StudySessionEntity
import com.example.data.local.entity.UserProfileEntity

@Database(
    entities = [
        UserProfileEntity::class,
        StudySessionEntity::class,
        DailyQuestEntity::class,
        AchievementEntity::class,
        ShopItemEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
    abstract fun studySessionDao(): StudySessionDao
    abstract fun dailyQuestDao(): DailyQuestDao
    abstract fun achievementDao(): AchievementDao
    abstract fun shopItemDao(): ShopItemDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "study_quest_database"
                )
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
