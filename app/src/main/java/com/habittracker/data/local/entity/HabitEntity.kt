package com.habittracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.habittracker.domain.model.Habit

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val name: String,
    val emoji: String,
    val colorHex: String,
    val frequencyDays: String, // JSON array string
    val reminderTime: String?,
    val createdAt: Long,
    val updatedAt: Long,
    val isArchived: Boolean,
    val isDeleted: Boolean,
    val pendingSync: Boolean,
    val isTrackerEnabled: Boolean
) {
    fun toDomain(): Habit {
        val days = try {
            frequencyDays.removeSurrounding("[", "]")
                .split(",")
                .map { it.trim().toInt() }
        } catch (e: Exception) {
            emptyList()
        }
        return Habit(
            id = id,
            userId = userId,
            name = name,
            emoji = emoji,
            colorHex = colorHex,
            frequencyDays = days,
            reminderTime = reminderTime,
            createdAt = createdAt,
            updatedAt = updatedAt,
            isArchived = isArchived,
            isDeleted = isDeleted,
            pendingSync = pendingSync,
            isTrackerEnabled = isTrackerEnabled
        )
    }

    companion object {
        fun fromDomain(habit: Habit): HabitEntity {
            return HabitEntity(
                id = habit.id,
                userId = habit.userId,
                name = habit.name,
                emoji = habit.emoji,
                colorHex = habit.colorHex,
                frequencyDays = habit.frequencyDays.joinToString(prefix = "[", postfix = "]"),
                reminderTime = habit.reminderTime,
                createdAt = habit.createdAt,
                updatedAt = habit.updatedAt,
                isArchived = habit.isArchived,
                isDeleted = habit.isDeleted,
                pendingSync = habit.pendingSync,
                isTrackerEnabled = habit.isTrackerEnabled
            )
        }
    }
}
