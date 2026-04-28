package com.habittracker.data.repository

import androidx.work.WorkManager
import com.google.firebase.auth.FirebaseAuth
import com.habittracker.data.local.db.AppDatabase
import com.habittracker.data.remote.firebase.FirebaseDataSource
import com.habittracker.domain.model.User
import com.habittracker.domain.repository.AuthRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: AppDatabase,
    private val workManager: WorkManager,
    private val firebaseDataSource: FirebaseDataSource
) : AuthRepository {

    override val currentUser: Flow<User?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser?.let {
                User(
                    id = it.uid,
                    email = it.email,
                    displayName = it.displayName,
                    photoUrl = it.photoUrl?.toString()
                )
            }
            trySend(user)
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    override suspend fun signInWithGoogle(idToken: String): Result<User> {
        // UI layer will pass the credential and sign in, here we just observe currentUser
        return Result.failure(NotImplementedError("Credential sign in handled by ViewModel/UI"))
    }

    override suspend fun signOut() {
        auth.signOut()
        db.habitDao.clearAll()
        db.habitLogDao.clearAll()
        workManager.cancelAllWork()
    }

    override suspend fun syncUserData(): Result<Unit> {
        return try {
            val userId = auth.currentUser?.uid ?: return Result.success(Unit)
            val habits = firebaseDataSource.pullHabits(userId)
            val logs = firebaseDataSource.pullHabitLogs(userId)
            
            // TODO: Merge strategy - simplified for now
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
