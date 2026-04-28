package com.habittracker.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.habittracker.data.local.db.HabitDao
import com.habittracker.data.local.db.HabitLogDao
import com.habittracker.data.remote.firebase.FirebaseDataSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val habitDao: HabitDao,
    private val habitLogDao: HabitLogDao,
    private val firebaseDataSource: FirebaseDataSource
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            // Push unsynced habits
            val unsyncedHabits = habitDao.getUnsyncedHabits()
            for (habitEntity in unsyncedHabits) {
                firebaseDataSource.pushHabit(habitEntity.toDomain())
                habitDao.insertHabit(habitEntity.copy(pendingSync = false))
            }

            // Push unsynced logs
            val unsyncedLogs = habitLogDao.getUnsyncedLogs()
            for (logEntity in unsyncedLogs) {
                firebaseDataSource.pushHabitLog(logEntity.toDomain())
                habitLogDao.insertLog(logEntity.copy(pendingSync = false))
            }

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
