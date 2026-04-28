package com.habittracker.presentation.settings

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.habittracker.data.worker.SyncWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val workManager: WorkManager
) : ViewModel() {

    fun triggerManualSync(context: Context) {
        val request = OneTimeWorkRequestBuilder<SyncWorker>().build()
        workManager.enqueue(request)
        Toast.makeText(context, "Cloud Sync triggered in background...", Toast.LENGTH_SHORT).show()
    }

    fun initiateGoogleSignIn(context: Context) {
        // Without a real google-services.json and SHA-1 fingerprint registered
        // in your Firebase Console, the Google Sign-In API will throw an ApiException.
        // For a production app, you would use CredentialManager or GoogleSignInClient here.
        Toast.makeText(context, "Real Google Sign-In requires SHA-1 Fingerprint setup in Firebase Console.", Toast.LENGTH_LONG).show()
    }
}
