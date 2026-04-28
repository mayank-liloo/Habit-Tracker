package com.habittracker.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.habittracker.data.local.entity.HabitEntity
import com.habittracker.data.local.entity.HabitLogEntity

@Database(
    entities = [HabitEntity::class, HabitLogEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract val habitDao: HabitDao
    abstract val habitLogDao: HabitLogDao
}
