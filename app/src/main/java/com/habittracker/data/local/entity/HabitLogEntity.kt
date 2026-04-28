package com.habittracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.habittracker.domain.model.HabitLog

@Entity(tableName = "habit_logs")
data class HabitLogEntity(
    @PrimaryKey val id: String,
    val habitId: String,
    val userId: String,
    val completedDate: String,
    val note: String?,
    val createdAt: Long,
    val updatedAt: Long,
    val isDeleted: Boolean,
    val pendingSync: Boolean
) {
    fun toDomain(): HabitLog {
        return HabitLog(
            id = id,
            habitId = habitId,
            userId = userId,
            completedDate = completedDate,
            note = note,
            createdAt = createdAt,
            updatedAt = updatedAt,
            isDeleted = isDeleted,
            pendingSync = pendingSync
        )
    }

    companion object {
        fun fromDomain(log: HabitLog): HabitLogEntity {
            return HabitLogEntity(
                id = log.id,
                habitId = log.habitId,
                userId = log.userId,
                completedDate = log.completedDate,
                note = log.note,
                createdAt = log.createdAt,
                updatedAt = log.updatedAt,
                isDeleted = log.isDeleted,
                pendingSync = log.pendingSync
            )
        }
    }
}
