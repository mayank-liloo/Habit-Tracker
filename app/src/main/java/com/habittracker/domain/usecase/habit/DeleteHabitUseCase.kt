package com.habittracker.domain.usecase.habit

import com.habittracker.domain.repository.HabitRepository
import javax.inject.Inject

class DeleteHabitUseCase @Inject constructor(
    private val habitRepository: HabitRepository
) {
    suspend operator fun invoke(habitId: String) {
        habitRepository.deleteHabit(habitId)
    }
}
