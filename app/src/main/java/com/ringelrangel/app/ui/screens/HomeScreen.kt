@file:OptIn(ExperimentalMaterial3Api::class)

package com.ringelrangel.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ringelrangel.app.BuildConfig
import com.ringelrangel.app.data.ExerciseRepository
import com.ringelrangel.app.data.MuscleGroup
import com.ringelrangel.app.data.Workout

@Composable
fun HomeScreen(
    onWorkoutClick: (String) -> Unit,
    onSettingsClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 16.dp, top = 56.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "RINGELRANGEL",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFF555555),
                        letterSpacing = 3.sp
                    )
                    Text(
                        text = "Ring Training",
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color.White
                    )
                }
                IconButton(onClick = onSettingsClick) {
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = "Einstellungen",
                        tint = Color(0xFF666666)
                    )
                }
            }
        }

        // Stats row
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                StatItem("${ExerciseRepository.workouts.size}", "Workouts")
                StatItem("${ExerciseRepository.exercises.size}", "Übungen")
                StatItem("3", "Level")
            }
        }

        // Divider
        item {
            Divider(
                color = Color(0xFF1A1A1A),
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }

        // Section: Workouts
        item {
            Text(
                text = "WORKOUTS",
                style = MaterialTheme.typography.labelMedium,
                color = Color(0xFF555555),
                letterSpacing = 3.sp,
                modifier = Modifier.padding(start = 24.dp, top = 28.dp, bottom = 16.dp)
            )
        }

        items(ExerciseRepository.workouts) { workout ->
            WorkoutRow(
                workout = workout,
                onClick = { onWorkoutClick(workout.id) }
            )
        }

        // Section: Muskelgruppen
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Divider(
                color = Color(0xFF1A1A1A),
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            Text(
                text = "MUSKELGRUPPEN",
                style = MaterialTheme.typography.labelMedium,
                color = Color(0xFF555555),
                letterSpacing = 3.sp,
                modifier = Modifier.padding(start = 24.dp, top = 28.dp, bottom = 12.dp)
            )
        }

        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val groups = MuscleGroup.entries.filter { group ->
                    ExerciseRepository.exercises.any { group in it.muscleGroups }
                }
                items(groups) { group ->
                    val count = ExerciseRepository.exercises.count { group in it.muscleGroups }
                    MuscleGroupChip(group.displayName, count)
                }
            }
        }

        // Version footer
        item {
            Text(
                text = "v${BuildConfig.VERSION_NAME}",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF333333),
                modifier = Modifier.padding(start = 24.dp, top = 32.dp)
            )
        }
    }
}

@Composable
private fun StatItem(number: String, label: String) {
    Column {
        Text(
            text = number,
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF555555)
        )
    }
}

@Composable
private fun WorkoutRow(workout: Workout, onClick: () -> Unit) {
    val borderColor = Color(0xFF1A1A1A)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .drawBehind {
                drawLine(
                    color = borderColor,
                    start = Offset(72f, size.height),
                    end = Offset(size.width - 72f, size.height),
                    strokeWidth = 1f
                )
            }
            .padding(horizontal = 24.dp, vertical = 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = workout.name,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            Text(
                text = "${workout.exercises.size} Übungen",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF555555)
            )
        }
        Text(
            text = workout.category.displayName.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF444444),
            letterSpacing = 2.sp
        )
    }
}

@Composable
private fun MuscleGroupChip(name: String, count: Int) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = Color(0xFF111111)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFAAAAAA)
            )
            Text(
                text = "$count",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF555555)
            )
        }
    }
}
