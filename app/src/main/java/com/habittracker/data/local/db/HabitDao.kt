package com.habittracker.data.local.db

import androidx.room.*
import com.habittracker.data.local.entity.HabitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits WHERE userId = :userId AND isDeleted = 0 ORDER BY createdAt DESC")
    fun getHabits(userId: String): Flow<List<HabitEntity>>

    @Query("SELECT * FROM habits WHERE id = :id AND isDeleted = 0")
    suspend fun getHabitById(id: String): HabitEntity?

    @Query("SELECT * FROM habits WHERE pendingSync = 1")
    suspend fun getUnsyncedHabits(): List<HabitEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: HabitEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabits(habits: List<HabitEntity>)

    @Query("UPDATE habits SET isDeleted = 1, pendingSync = 1, updatedAt = :updatedAt WHERE id = :id")
    suspend fun softDeleteHabit(id: String, updatedAt: Long)
    
    @Query("DELETE FROM habits WHERE id = :id")
    suspend fun hardDeleteHabit(id: String)

    @Query("DELETE FROM habits")
    suspend fun clearAll()
}
