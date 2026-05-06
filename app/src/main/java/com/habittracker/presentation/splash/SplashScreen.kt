package com.habittracker.presentation.splash

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.size
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashComplete: () -> Unit
) {
    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000)
    )
    val scaleAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.5f,
        animationSpec = tween(durationMillis = 1000)
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(1500L) // Wait 1.5 seconds
        onSplashComplete()
    }

    val SplashBlue = Color(0xFF2B95F5)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SplashBlue),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = com.habittracker.R.drawable.splash_logo),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(200.dp)
                .scale(scaleAnim.value)
                .alpha(alphaAnim.value)
        )
    }
}
