package com.habittracker.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.habittracker.domain.model.Habit
import com.habittracker.domain.repository.AuthRepository
import com.habittracker.domain.repository.HabitRepository
import com.habittracker.domain.usecase.habit.ToggleHabitCompletionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class DashboardUiState(
    val isLoading: Boolean = true,
    val habitsWithLogs: List<Pair<Habit, Boolean>> = emptyList(),
    val todayDate: LocalDate = LocalDate.now(),
    val completionPercentage: Float = 0f,
    val userName: String? = null
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val habitRepository: HabitRepository,
    private val toggleHabitCompletionUseCase: ToggleHabitCompletionUseCase
) : ViewModel() {

    private val _currentDate = MutableStateFlow(LocalDate.now())

    val uiState: StateFlow<DashboardUiState> = combine(
        authRepository.currentUser,
        _currentDate.flatMapLatest { date ->
            authRepository.currentUser.flatMapLatest { user ->
                val uid = user?.id ?: "offline_user_id"
                combine(
                    habitRepository.getHabits(uid),
                    habitRepository.getAllLogs(uid)
                ) { habits, allLogs ->
                    val dateString = date.toString()
                    habits.map { habit ->
                        val isCompleted = allLogs.any { it.habitId == habit.id && it.completedDate == dateString }
                        habit to isCompleted
                    }
                }
            }
        },
        _currentDate
    ) { user, habitsWithStatus, date ->
        val completedCount = habitsWithStatus.count { it.second }
        val totalCount = habitsWithStatus.size
        val percentage = if (totalCount == 0) 0f else completedCount.toFloat() / totalCount

        DashboardUiState(
            isLoading = false,
            habitsWithLogs = habitsWithStatus,
            todayDate = date,
            completionPercentage = percentage,
            userName = user?.displayName ?: "Explorer"
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DashboardUiState())

    fun toggleHabit(habitId: String) {
        viewModelScope.launch {
            val user = authRepository.currentUser.first()
            val uid = user?.id ?: "offline_user_id"
            toggleHabitCompletionUseCase(habitId, uid, _currentDate.value)
        }
    }
}
