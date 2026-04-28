package com.habittracker.widget

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.Text
import androidx.glance.text.TextStyle

class HabitWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            Column(
                modifier = GlanceModifier.fillMaxSize()
                    .background(Color(0xFF18181B)) // Aesthetic Dark Surface
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Habit Tracker",
                    style = TextStyle(color = androidx.glance.color.ColorProvider(day = Color.White, night = Color.White))
                )
                Text(
                    text = "Stay consistent today!",
                    style = TextStyle(color = androidx.glance.color.ColorProvider(day = Color.Gray, night = Color.Gray))
                )
            }
        }
    }
}
