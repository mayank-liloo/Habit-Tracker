package com.habittracker.data.local.db

import androidx.room.*
import com.habittracker.data.local.entity.HabitLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitLogDao {
    @Query("SELECT * FROM habit_logs WHERE habitId = :habitId AND isDeleted = 0 ORDER BY completedDate DESC")
    fun getLogsForHabit(habitId: String): Flow<List<HabitLogEntity>>

    @Query("SELECT * FROM habit_logs WHERE userId = :userId AND isDeleted = 0")
    fun getAllLogs(userId: String): Flow<List<HabitLogEntity>>

    @Query("SELECT * FROM habit_logs WHERE pendingSync = 1")
    suspend fun getUnsyncedLogs(): List<HabitLogEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: HabitLogEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLogs(logs: List<HabitLogEntity>)

    @Query("UPDATE habit_logs SET isDeleted = 1, pendingSync = 1, updatedAt = :updatedAt WHERE id = :id")
    suspend fun softDeleteLog(id: String, updatedAt: Long)
    
    @Query("DELETE FROM habit_logs WHERE id = :id")
    suspend fun hardDeleteLog(id: String)

    @Query("DELETE FROM habit_logs")
    suspend fun clearAll()
}
