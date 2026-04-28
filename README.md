# 🌌 HabitTracker Pro

An aesthetic, high-performance, and offline-first Android Habit Tracking application built from scratch to demonstrate production-grade modern Android development practices. 

## ✨ Features
* **Premium Dark Aesthetic UI**: Built entirely with Jetpack Compose (Material 3) utilizing fluid micro-animations and a bespoke design system.
* **Offline-First Architecture**: Powered by a local SQLite Room Database acting as the single source of truth.
* **Cloud Syncing**: Background synchronization to Firebase Firestore using Android `WorkManager`.
* **Hardware Sensor Integration**: Built-in "Running Tracker" that hooks into the Android `SensorManager` (`TYPE_STEP_COUNTER`) to track physical steps and calculate estimated distance natively.
* **Canvas Heatmaps**: Custom-drawn, GitHub-style contribution heatmaps rendered from scratch using the Compose Canvas API.
* **Background Notifications**: Scheduled push notifications to remind users of uncompleted habits.
* **Home Screen Widget**: Sleek, Glance API-powered widget for checking habit status directly from the Android launcher.
* **Onboarding & Preferences**: First-time user experience managed flawlessly with Jetpack DataStore.

## 🛠 Tech Stack
* **Language**: Kotlin
* **UI**: Jetpack Compose, Material 3, Jetpack Navigation, Glance API
* **Architecture**: Clean Architecture (Data/Domain/Presentation), MVVM
* **Dependency Injection**: Dagger Hilt
* **Async & State**: Kotlin Coroutines & StateFlow
* **Persistence**: Room Database, Jetpack DataStore
* **Background Processing**: WorkManager
* **Cloud & Auth**: Firebase Firestore, Firebase Auth (Google Sign-in)

## 🏗 Architecture & Offline-First Strategy
This app follows strict Clean Architecture principles. 
1. The **Data Layer** handles local caching (Room) and remote synchronization.
2. The **Domain Layer** contains the core business logic (Use Cases) and pure Kotlin models.
3. The **Presentation Layer** strictly observes `StateFlow` from the ViewModels to render the UI.

The app is fully functional without an internet connection. If the user creates a habit while offline, the repository saves it locally and enqueues a `SyncWorker`. Once the device regains network connectivity, WorkManager automatically syncs the local Room database to the Firebase Firestore cloud.

## 🚀 Getting Started
To compile and run this project:
1. Clone the repository.
2. Open the project in **Android Studio (Ladybug or newer)**.
3. Sync Gradle.
4. *Optional*: For cloud syncing, create a Firebase project, register `com.habittracker` as the package name, and place your `google-services.json` file in the `app/` directory.
5. Click **Run**.
