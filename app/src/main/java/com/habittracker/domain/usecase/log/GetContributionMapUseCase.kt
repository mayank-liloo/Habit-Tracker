package com.habittracker.domain.usecase.log

import com.habittracker.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class GetContributionMapUseCase @Inject constructor(
    private val habitRepository: HabitRepository
) {
    operator fun invoke(userId: String): Flow<Map<LocalDate, Int>> {
        return habitRepository.getAllLogs(userId).map { logs ->
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val counts = mutableMapOf<LocalDate, Int>()
            logs.forEach { log ->
                val date = try { LocalDate.parse(log.completedDate, formatter) } catch(e: Exception) { null }
                if (date != null) {
                    counts[date] = counts.getOrDefault(date, 0) + 1
                }
            }
            counts
        }
    }
}
