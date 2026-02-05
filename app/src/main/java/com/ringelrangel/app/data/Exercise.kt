package com.ringelrangel.app.data

data class Exercise(
    val id: String,
    val name: String,
    val description: String,
    val muscleGroups: List<MuscleGroup>,
    val difficulty: Difficulty,
    val defaultSets: Int = 3,
    val defaultReps: String = "8-12",
    val restSeconds: Int = 90,
    val instructions: List<String> = emptyList(),
    val imageRes: String? = null
)

enum class MuscleGroup(val displayName: String) {
    CHEST("Brust"),
    BACK("Rücken"),
    SHOULDERS("Schultern"),
    BICEPS("Bizeps"),
    TRICEPS("Trizeps"),
    CORE("Core"),
    LEGS("Beine"),
    FULL_BODY("Ganzkörper")
}

enum class Difficulty(val displayName: String, val level: Int) {
    BEGINNER("Anfänger", 1),
    INTERMEDIATE("Fortgeschritten", 2),
    ADVANCED("Profi", 3)
}

data class Workout(
    val id: String,
    val name: String,
    val description: String,
    val exercises: List<WorkoutExercise>,
    val category: WorkoutCategory
)

data class WorkoutExercise(
    val exercise: Exercise,
    val sets: Int,
    val reps: String,
    val restSeconds: Int
)

enum class WorkoutCategory(val displayName: String) {
    PUSH("Push"),
    PULL("Pull"),
    FULL_BODY("Ganzkörper"),
    UPPER_BODY("Oberkörper"),
    CORE("Core")
}
