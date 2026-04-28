package com.habittracker.domain.usecase.habit

import com.habittracker.domain.model.HabitLog
import com.habittracker.domain.repository.HabitRepository
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

class ToggleHabitCompletionUseCase @Inject constructor(
    private val habitRepository: HabitRepository
) {
    suspend operator fun invoke(habitId: String, userId: String, date: LocalDate) {
        val dateString = date.toString()
        val logs = habitRepository.getLogsForHabit(habitId).first()
        val existingLog = logs.find { it.completedDate == dateString }

        if (existingLog != null) {
            habitRepository.deleteLog(existingLog.id)
        } else {
            habitRepository.insertLog(
                HabitLog(
                    id = UUID.randomUUID().toString(),
                    habitId = habitId,
                    userId = userId,
                    completedDate = dateString,
                    note = null,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                    isDeleted = false,
                    pendingSync = true
                )
            )
        }
    }
}
