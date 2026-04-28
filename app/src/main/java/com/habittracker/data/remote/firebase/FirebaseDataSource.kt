package com.habittracker.data.remote.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.habittracker.domain.model.Habit
import com.habittracker.domain.model.HabitLog
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun pushHabit(habit: Habit) {
        firestore.collection("users")
            .document(habit.userId)
            .collection("habits")
            .document(habit.id)
            .set(habit, SetOptions.merge())
            .await()
    }

    suspend fun pushHabitLog(log: HabitLog) {
        firestore.collection("users")
            .document(log.userId)
            .collection("habit_logs")
            .document(log.id)
            .set(log, SetOptions.merge())
            .await()
    }

    suspend fun pullHabits(userId: String): List<Habit> {
        val snapshot = firestore.collection("users")
            .document(userId)
            .collection("habits")
            .get()
            .await()
            
        return snapshot.documents.mapNotNull { it.toObject(Habit::class.java) }
    }

    suspend fun pullHabitLogs(userId: String): List<HabitLog> {
        val snapshot = firestore.collection("users")
            .document(userId)
            .collection("habit_logs")
            .get()
            .await()
            
        return snapshot.documents.mapNotNull { it.toObject(HabitLog::class.java) }
    }
}
