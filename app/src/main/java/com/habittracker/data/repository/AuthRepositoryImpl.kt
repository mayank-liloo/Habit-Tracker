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
import kotlinx.coroutines.tasks.await
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
        return Result.failure(NotImplementedError("Credential sign in handled by ViewModel/UI"))
    }

    override suspend fun signInWithEmail(email: String, pass: String): Result<User> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, pass).await()
            val fbUser = result.user ?: throw Exception("Login failed: User is null")
            Result.success(
                User(id = fbUser.uid, email = fbUser.email, displayName = fbUser.displayName, photoUrl = fbUser.photoUrl?.toString())
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun registerWithEmail(email: String, pass: String, name: String): Result<User> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, pass).await()
            val fbUser = result.user ?: throw Exception("Registration failed: User is null")
            
            val profileUpdates = com.google.firebase.auth.userProfileChangeRequest {
                displayName = name
            }
            fbUser.updateProfile(profileUpdates).await()
            
            Result.success(
                User(id = fbUser.uid, email = fbUser.email, displayName = name, photoUrl = null)
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
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
