@file:OptIn(ExperimentalMaterial3Api::class)

package com.ringelrangel.app.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ringelrangel.app.data.Difficulty
import com.ringelrangel.app.data.ExerciseRepository
import com.ringelrangel.app.data.WorkoutExercise

@Composable
fun WorkoutDetailScreen(
    workoutId: String,
    onBack: () -> Unit
) {
    val workout = ExerciseRepository.workouts.find { it.id == workoutId }

    if (workout == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Workout nicht gefunden", color = Color(0xFF555555))
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 48.dp)
    ) {
        // Back button + Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 24.dp, top = 48.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Zurück",
                        tint = Color(0xFF666666)
                    )
                }
            }
        }

        item {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
            ) {
                Text(
                    text = workout.category.displayName.uppercase(),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF555555),
                    letterSpacing = 3.sp
                )
                Text(
                    text = workout.name,
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Text(
                    text = workout.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF666666),
                    modifier = Modifier.padding(top = 4.dp)
                )

                // Stats
                Row(
                    modifier = Modifier.padding(top = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    MiniStat("${workout.exercises.size}", "Übungen")
                    MiniStat(
                        "${workout.exercises.sumOf { it.sets }}",
                        "Sätze"
                    )
                    val totalRest = workout.exercises.sumOf { it.sets * it.restSeconds }
                    MiniStat("~${totalRest / 60}min", "Pause")
                }
            }
        }

        item {
            Divider(
                color = Color(0xFF1A1A1A),
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp)
            )
        }

        // Exercise list
        itemsIndexed(workout.exercises) { index, we ->
            ExerciseRow(
                number = index + 1,
                workoutExercise = we,
                isLast = index == workout.exercises.lastIndex
            )
        }
    }
}

@Composable
private fun MiniStat(value: String, label: String) {
    Column {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
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
private fun ExerciseRow(
    number: Int,
    workoutExercise: WorkoutExercise,
    isLast: Boolean
) {
    var expanded by remember { mutableStateOf(false) }
    val exercise = workoutExercise.exercise
    val borderColor = Color(0xFF1A1A1A)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable { expanded = !expanded }
            .then(
                if (!isLast) Modifier.drawBehind {
                    drawLine(
                        color = borderColor,
                        start = Offset(72f, size.height),
                        end = Offset(size.width - 72f, size.height),
                        strokeWidth = 1f
                    )
                } else Modifier
            )
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            // Number
            Text(
                text = String.format("%02d", number),
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF333333),
                modifier = Modifier
                    .width(28.dp)
                    .padding(top = 2.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = exercise.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "${workoutExercise.sets} × ${workoutExercise.reps}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF666666)
                    )
                    Text(
                        text = "${workoutExercise.restSeconds}s",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF444444)
                    )
                }
            }

            DifficultyDot(exercise.difficulty)
        }

        // Expanded content
        if (expanded) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = exercise.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF888888),
                modifier = Modifier.padding(start = 28.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))
            exercise.instructions.forEachIndexed { i, step ->
                Row(
                    modifier = Modifier.padding(start = 28.dp, bottom = 6.dp)
                ) {
                    Text(
                        text = "${i + 1}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF333333),
                        modifier = Modifier.width(20.dp)
                    )
                    Text(
                        text = step,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF777777)
                    )
                }
            }

            // Muscle groups
            Row(
                modifier = Modifier.padding(start = 28.dp, top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                exercise.muscleGroups.forEach { group ->
                    Text(
                        text = group.displayName.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF444444),
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun DifficultyDot(difficulty: Difficulty) {
    val color = when (difficulty) {
        Difficulty.BEGINNER -> Color(0xFF444444)
        Difficulty.INTERMEDIATE -> Color(0xFF777777)
        Difficulty.ADVANCED -> Color.White
    }

    Surface(
        modifier = Modifier.size(8.dp),
        shape = MaterialTheme.shapes.small,
        color = color
    ) {}
}
