package com.habittracker.presentation.history

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Journey", fontWeight = FontWeight.Bold) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            HeatmapCard(uiState.contributionMap, uiState.maxCompletionsInADay)
        }
    }
}

@Composable
fun HeatmapCard(contributionMap: Map<LocalDate, Int>, maxCompletions: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Contribution Heatmap (Last 90 Days)",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            val primaryColor = MaterialTheme.colorScheme.primary
            val emptyColor = MaterialTheme.colorScheme.surfaceVariant
            
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                val boxSize = 12.dp.toPx()
                val spacing = 4.dp.toPx()
                val cols = 13 // approx 90 days / 7
                val rows = 7
                
                val startX = 0f
                val startY = 0f
                val today = LocalDate.now()

                for (col in 0 until cols) {
                    for (row in 0 until rows) {
                        val daysAgo = ((cols - 1 - col) * 7) + (6 - row)
                        val date = today.minusDays(daysAgo.toLong())
                        
                        val count = contributionMap[date] ?: 0
                        val intensity = if (count == 0) 0f else (count.toFloat() / maxCompletions).coerceIn(0.2f, 1f)
                        
                        val color = if (count == 0) emptyColor else primaryColor.copy(alpha = intensity)
                        
                        drawRoundRect(
                            color = color,
                            topLeft = Offset(
                                x = startX + col * (boxSize + spacing),
                                y = startY + row * (boxSize + spacing)
                            ),
                            size = Size(boxSize, boxSize),
                            cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                        )
                    }
                }
            }
        }
    }
}
