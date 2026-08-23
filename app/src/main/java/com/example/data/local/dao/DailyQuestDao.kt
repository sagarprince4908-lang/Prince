package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.DailyQuestEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyQuestDao {
    @Query("SELECT * FROM daily_quests WHERE questDate = :dateStr")
    fun getQuestsForDate(dateStr: String): Flow<List<DailyQuestEntity>>

    @Query("SELECT * FROM daily_quests WHERE questDate = :dateStr")
    suspend fun getQuestsForDateDirect(dateStr: String): List<DailyQuestEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuests(quests: List<DailyQuestEntity>)

    @Update
    suspend fun updateQuest(quest: DailyQuestEntity)

    @Query("UPDATE daily_quests SET currentValue = :value, isCompleted = :isCompleted WHERE id = :questId")
    suspend fun updateQuestProgress(questId: String, value: Int, isCompleted: Boolean)

    @Query("UPDATE daily_quests SET isClaimed = 1 WHERE id = :questId")
    suspend fun markClaimed(questId: String)
}
