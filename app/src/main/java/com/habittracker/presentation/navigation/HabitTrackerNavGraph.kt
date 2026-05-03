package com.habittracker.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.habittracker.presentation.addhabit.AddHabitScreen
import com.habittracker.presentation.dashboard.DashboardScreen
import com.habittracker.presentation.history.HistoryScreen
import com.habittracker.presentation.settings.SettingsScreen

@Composable
fun HabitTrackerNavGraph() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomBarRoutes = listOf("dashboard", "history", "settings")

    Scaffold(
        bottomBar = {
            if (currentRoute in bottomBarRoutes) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ) {
                    NavigationBarItem(
                        selected = currentRoute == "dashboard",
                        onClick = { navController.navigate("dashboard") { launchSingleTop = true } },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                        label = { Text("Home") }
                    )
                    NavigationBarItem(
                        selected = currentRoute == "history",
                        onClick = { navController.navigate("history") { launchSingleTop = true } },
                        icon = { Icon(Icons.Default.DateRange, contentDescription = "History") },
                        label = { Text("History") }
                    )
                    NavigationBarItem(
                        selected = currentRoute == "settings",
                        onClick = { navController.navigate("settings") { launchSingleTop = true } },
                        icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                        label = { Text("Settings") }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController, 
            startDestination = "splash",
            modifier = Modifier.padding(padding)
        ) {
            composable("splash") {
                val viewModel: com.habittracker.presentation.splash.SplashViewModel = androidx.hilt.navigation.compose.hiltViewModel()
                val isLoading by viewModel.isLoading.collectAsState()
                val destination by viewModel.startDestination.collectAsState()
                
                com.habittracker.presentation.splash.SplashScreen(onSplashComplete = {
                    if (!isLoading) {
                        navController.navigate(destination) {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                })
            }
            composable("onboarding") {
                com.habittracker.presentation.onboarding.OnboardingScreen(onFinishOnboarding = {
                    navController.navigate("login") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                })
            }
            composable("login") {
                com.habittracker.presentation.auth.LoginScreen(
                    onNavigateToRegister = { navController.navigate("register") },
                    onLoginSuccess = {
                        navController.navigate("dashboard") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }
            composable("register") {
                com.habittracker.presentation.auth.RegisterScreen(
                    onNavigateBackToLogin = { navController.popBackStack() },
                    onRegisterSuccess = {
                        navController.navigate("dashboard") {
                            popUpTo("register") { inclusive = true }
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }
            composable("dashboard") {
                DashboardScreen(
                    onAddHabitClick = { navController.navigate("add_habit") },
                    onHabitClick = { habitId -> navController.navigate("habit_detail/$habitId") },
                    onTrackerClick = { habitId -> navController.navigate("tracker/$habitId") }
                )
            }
            composable("history") {
                HistoryScreen()
            }
            composable("settings") {
                SettingsScreen()
            }
            composable("add_habit") {
                AddHabitScreen(onNavigateBack = { navController.popBackStack() })
            }
            composable(
                route = "habit_detail/{habitId}",
                arguments = listOf(androidx.navigation.navArgument("habitId") { type = androidx.navigation.NavType.StringType })
            ) {
                com.habittracker.presentation.habitdetail.HabitDetailScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable(
                route = "tracker/{habitId}",
                arguments = listOf(androidx.navigation.navArgument("habitId") { type = androidx.navigation.NavType.StringType })
            ) {
                com.habittracker.presentation.tracker.TrackerScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
