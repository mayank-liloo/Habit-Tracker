package com.habittracker.domain.repository

import com.habittracker.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: Flow<User?>
    suspend fun signInWithGoogle(idToken: String): Result<User>
    suspend fun signInWithEmail(email: String, pass: String): Result<User>
    suspend fun registerWithEmail(email: String, pass: String, name: String): Result<User>
    suspend fun signOut()
    suspend fun syncUserData(): Result<Unit>
}
