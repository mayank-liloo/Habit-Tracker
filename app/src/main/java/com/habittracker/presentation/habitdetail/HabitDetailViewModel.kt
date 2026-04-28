package com.habittracker.presentation.habitdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.habittracker.domain.model.Habit
import com.habittracker.domain.repository.HabitRepository
import com.habittracker.domain.usecase.habit.DeleteHabitUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val habitRepository: HabitRepository,
    private val deleteHabitUseCase: DeleteHabitUseCase
) : ViewModel() {

    private val habitId: String = checkNotNull(savedStateHandle["habitId"])
    
    private val _habit = MutableStateFlow<Habit?>(null)
    val habit = _habit.asStateFlow()

    init {
        viewModelScope.launch {
            _habit.value = habitRepository.getHabitById(habitId)
        }
    }
    
    fun deleteHabit(onSuccess: () -> Unit) {
        viewModelScope.launch {
            deleteHabitUseCase(habitId)
            onSuccess()
        }
    }
}
