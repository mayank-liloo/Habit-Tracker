package com.habittracker.presentation.tracker

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.habittracker.domain.model.Habit
import com.habittracker.domain.model.HabitLog
import com.habittracker.domain.repository.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class TrackerViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle,
    private val habitRepository: HabitRepository
) : ViewModel(), SensorEventListener {

    private val habitId: String = checkNotNull(savedStateHandle["habitId"])
    
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val stepSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

    private val _habit = MutableStateFlow<Habit?>(null)
    val habit = _habit.asStateFlow()

    private val _steps = MutableStateFlow(0)
    val steps = _steps.asStateFlow()

    private val _distanceMeters = MutableStateFlow(0f)
    val distanceMeters = _distanceMeters.asStateFlow()

    private val _isTracking = MutableStateFlow(false)
    val isTracking = _isTracking.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private var initialSteps: Int? = null

    init {
        viewModelScope.launch {
            _habit.value = habitRepository.getHabitById(habitId)
        }
    }

    fun startTracking() {
        if (stepSensor != null) {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
            _isTracking.value = true
            _error.value = null
        } else {
            _error.value = "Hardware Step Sensor not available on this device."
        }
    }

    fun stopTracking() {
        sensorManager.unregisterListener(this)
        _isTracking.value = false
    }

    fun finishWorkout(onSuccess: () -> Unit) {
        stopTracking()
        viewModelScope.launch {
            val user = _habit.value?.userId ?: return@launch
            val log = HabitLog(
                id = UUID.randomUUID().toString(),
                habitId = habitId,
                userId = user,
                completedDate = LocalDate.now().toString(),
                note = "Ran ${String.format("%.2f", _distanceMeters.value / 1000f)} km (${_steps.value} steps)",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
                isDeleted = false,
                pendingSync = true
            )
            habitRepository.insertLog(log)
            onSuccess()
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            val totalSteps = event.values[0].toInt()
            if (initialSteps == null) {
                initialSteps = totalSteps
            }
            
            val currentSteps = totalSteps - (initialSteps ?: totalSteps)
            if (currentSteps >= 0) {
                _steps.value = currentSteps
                _distanceMeters.value = currentSteps * 0.762f // Approx stride length
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onCleared() {
        super.onCleared()
        stopTracking()
    }
}
