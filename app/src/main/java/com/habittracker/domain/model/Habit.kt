package com.habittracker.domain.model

data class Habit(
    val id: String,
    val userId: String,
    val name: String,
    val emoji: String,
    val colorHex: String,
    val frequencyDays: List<Int>, // e.g., 1=Mon, 7=Sun
    val reminderTime: String?, // "HH:mm" format or null
    val createdAt: Long,
    val updatedAt: Long,
    val isArchived: Boolean,
    val isDeleted: Boolean,
    val pendingSync: Boolean = false,
    val isTrackerEnabled: Boolean = false
)
