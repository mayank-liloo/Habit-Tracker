package com.habittracker.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.habittracker.data.local.preferences.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

import kotlinx.coroutines.flow.first

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    private val authRepository: com.habittracker.domain.repository.AuthRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _startDestination = MutableStateFlow("onboarding")
    val startDestination = _startDestination.asStateFlow()

    init {
        viewModelScope.launch {
            userPreferences.onboardingCompleted.collect { hasSeen ->
                if (hasSeen) {
                    val user = authRepository.currentUser.first()
                    if (user != null) {
                        _startDestination.value = "dashboard"
                    } else {
                        _startDestination.value = "login"
                    }
                } else {
                    _startDestination.value = "onboarding"
                }
                _isLoading.value = false
            }
        }
    }
}
