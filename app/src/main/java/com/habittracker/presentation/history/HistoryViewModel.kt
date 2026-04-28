package com.habittracker.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.habittracker.domain.repository.AuthRepository
import com.habittracker.domain.usecase.log.GetContributionMapUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import java.time.LocalDate
import javax.inject.Inject

data class HistoryUiState(
    val contributionMap: Map<LocalDate, Int> = emptyMap(),
    val maxCompletionsInADay: Int = 1
)

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val getContributionMapUseCase: GetContributionMapUseCase
) : ViewModel() {

    val uiState: StateFlow<HistoryUiState> = authRepository.currentUser
        .flatMapLatest { user ->
            val uid = user?.id ?: "offline_user_id"
            getContributionMapUseCase(uid)
        }.map { map ->
            val max = map.values.maxOrNull() ?: 1
            HistoryUiState(
                contributionMap = map,
                maxCompletionsInADay = if (max > 0) max else 1
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HistoryUiState())
}
