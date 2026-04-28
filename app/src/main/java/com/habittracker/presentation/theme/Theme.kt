package com.habittracker.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val AestheticDarkColorScheme = darkColorScheme(
    primary = AestheticPrimary,
    onPrimary = AestheticOnPrimary,
    primaryContainer = AestheticPrimaryContainer,
    onPrimaryContainer = AestheticOnPrimaryContainer,
    secondary = AestheticSecondary,
    onSecondary = AestheticOnSecondary,
    background = AestheticBackground,
    onBackground = AestheticTextPrimary,
    surface = AestheticSurface,
    onSurface = AestheticTextPrimary,
    surfaceVariant = AestheticSurfaceVariant,
    onSurfaceVariant = AestheticTextSecondary
)

@Composable
fun HabitTrackerTheme(
    content: @Composable () -> Unit
) {
    // We force the Aesthetic Dark Theme for a premium look
    MaterialTheme(
        colorScheme = AestheticDarkColorScheme,
        typography = AppTypography,
        content = content
    )
}
