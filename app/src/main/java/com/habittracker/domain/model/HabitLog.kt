package com.habittracker.domain.model

data class HabitLog(
    val id: String,
    val habitId: String,
    val userId: String,
    val completedDate: String, // "yyyy-MM-dd" format
    val note: String?,
    val createdAt: Long,
    val updatedAt: Long,
    val isDeleted: Boolean,
    val pendingSync: Boolean = false
)
