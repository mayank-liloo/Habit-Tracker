package com.habittracker.domain.usecase.habit

import com.habittracker.domain.model.Habit
import com.habittracker.domain.repository.HabitRepository
import java.util.UUID
import javax.inject.Inject

class AddHabitUseCase @Inject constructor(
    private val habitRepository: HabitRepository
) {
    suspend operator fun invoke(
        userId: String,
        name: String,
        emoji: String,
        colorHex: String,
        frequencyDays: List<Int>,
        reminderTime: String?,
        isTrackerEnabled: Boolean = false
    ) {
        val habit = Habit(
            id = UUID.randomUUID().toString(),
            userId = userId,
            name = name,
            emoji = emoji,
            colorHex = colorHex,
            frequencyDays = frequencyDays,
            reminderTime = reminderTime,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
            isArchived = false,
            isDeleted = false,
            pendingSync = true,
            isTrackerEnabled = isTrackerEnabled
        )
        habitRepository.insertHabit(habit)
    }
}
