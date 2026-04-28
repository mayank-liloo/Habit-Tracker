package com.habittracker.domain.repository

import com.habittracker.domain.model.Habit
import com.habittracker.domain.model.HabitLog
import kotlinx.coroutines.flow.Flow

interface HabitRepository {
    fun getHabits(userId: String): Flow<List<Habit>>
    suspend fun getHabitById(id: String): Habit?
    suspend fun insertHabit(habit: Habit)
    suspend fun updateHabit(habit: Habit)
    suspend fun deleteHabit(habitId: String)
    suspend fun archiveHabit(habitId: String)

    fun getLogsForHabit(habitId: String): Flow<List<HabitLog>>
    fun getAllLogs(userId: String): Flow<List<HabitLog>>
    suspend fun insertLog(log: HabitLog)
    suspend fun deleteLog(logId: String)
}
