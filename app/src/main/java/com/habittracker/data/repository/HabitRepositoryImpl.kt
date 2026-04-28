package com.habittracker.data.repository

import com.habittracker.data.local.db.HabitDao
import com.habittracker.data.local.db.HabitLogDao
import com.habittracker.data.local.entity.HabitEntity
import com.habittracker.data.local.entity.HabitLogEntity
import com.habittracker.domain.model.Habit
import com.habittracker.domain.model.HabitLog
import com.habittracker.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HabitRepositoryImpl @Inject constructor(
    private val habitDao: HabitDao,
    private val habitLogDao: HabitLogDao
) : HabitRepository {

    override fun getHabits(userId: String): Flow<List<Habit>> {
        return habitDao.getHabits(userId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getHabitById(id: String): Habit? {
        return habitDao.getHabitById(id)?.toDomain()
    }

    override suspend fun insertHabit(habit: Habit) {
        habitDao.insertHabit(HabitEntity.fromDomain(habit).copy(pendingSync = true))
    }

    override suspend fun updateHabit(habit: Habit) {
        habitDao.insertHabit(HabitEntity.fromDomain(habit).copy(pendingSync = true, updatedAt = System.currentTimeMillis()))
    }

    override suspend fun deleteHabit(habitId: String) {
        habitDao.softDeleteHabit(habitId, System.currentTimeMillis())
    }

    override suspend fun archiveHabit(habitId: String) {
        val habit = habitDao.getHabitById(habitId)
        if (habit != null) {
            habitDao.insertHabit(habit.copy(isArchived = true, pendingSync = true, updatedAt = System.currentTimeMillis()))
        }
    }

    override fun getLogsForHabit(habitId: String): Flow<List<HabitLog>> {
        return habitLogDao.getLogsForHabit(habitId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getAllLogs(userId: String): Flow<List<HabitLog>> {
        return habitLogDao.getAllLogs(userId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun insertLog(log: HabitLog) {
        habitLogDao.insertLog(HabitLogEntity.fromDomain(log).copy(pendingSync = true))
    }

    override suspend fun deleteLog(logId: String) {
        habitLogDao.softDeleteLog(logId, System.currentTimeMillis())
    }
}
