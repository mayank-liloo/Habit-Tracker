package com.habittracker.data.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.habittracker.domain.repository.AuthRepository
import com.habittracker.domain.repository.HabitRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.time.LocalDate

@HiltWorker
class ReminderWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted params: WorkerParameters,
    private val habitRepository: HabitRepository,
    private val authRepository: AuthRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val user = authRepository.currentUser.first()
        val userId = user?.id ?: "offline_user_id"

        val habits = habitRepository.getHabits(userId).first()
        val logs = habitRepository.getAllLogs(userId).first()
        val today = LocalDate.now().toString()

        val incompleteHabits = habits.filter { habit ->
            val isCompleted = logs.any { it.habitId == habit.id && it.completedDate == today }
            !isCompleted
        }

        if (incompleteHabits.isNotEmpty()) {
            showNotification(incompleteHabits.size)
        }

        return Result.success()
    }

    private fun showNotification(incompleteCount: Int) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "habit_reminders"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Habit Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setContentTitle("Stay Consistent! \uD83D\uDE80")
            .setContentText("You have $incompleteCount habits left to complete today.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        manager.notify(1001, notification)
    }
}
