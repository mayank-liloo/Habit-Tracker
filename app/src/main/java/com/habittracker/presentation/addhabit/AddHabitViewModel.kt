package com.habittracker.presentation.addhabit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.habittracker.domain.repository.AuthRepository
import com.habittracker.domain.usecase.habit.AddHabitUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddHabitViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val addHabitUseCase: AddHabitUseCase
) : ViewModel() {

    fun saveHabit(
        name: String,
        emoji: String,
        colorHex: String,
        frequencyDays: List<Int>,
        reminderTime: String?,
        isTrackerEnabled: Boolean,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val user = authRepository.currentUser.first()
            val userId = user?.id ?: "offline_user_id"
            
            addHabitUseCase(
                userId = userId,
                name = name,
                emoji = emoji,
                colorHex = colorHex,
                frequencyDays = frequencyDays,
                reminderTime = reminderTime,
                isTrackerEnabled = isTrackerEnabled
            )
            onSuccess()
        }
    }
}
