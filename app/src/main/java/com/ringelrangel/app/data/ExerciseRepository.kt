package com.ringelrangel.app.data

object ExerciseRepository {

    val exercises = listOf(
        // PUSH exercises
        Exercise(
            id = "ring_pushup",
            name = "Ring Liegestütze",
            description = "Liegestütze an den Ringen – instabiler als am Boden und dadurch deutlich effektiver.",
            muscleGroups = listOf(MuscleGroup.CHEST, MuscleGroup.TRICEPS, MuscleGroup.SHOULDERS),
            difficulty = Difficulty.BEGINNER,
            defaultSets = 3,
            defaultReps = "8-15",
            restSeconds = 90,
            instructions = listOf(
                "Ringe auf Bodenhöhe einstellen",
                "Hände in die Ringe, Körper gestreckt",
                "Langsam absenken bis Brust auf Ringhöhe",
                "Ringe nach außen drehen beim Hochdrücken (RTO)"
            )
        ),
        Exercise(
            id = "ring_dip",
            name = "Ring Dips",
            description = "Dips an den Ringen fordern die Stabilisatoren enorm und bauen Kraft in Brust, Trizeps und Schultern auf.",
            muscleGroups = listOf(MuscleGroup.CHEST, MuscleGroup.TRICEPS, MuscleGroup.SHOULDERS),
            difficulty = Difficulty.INTERMEDIATE,
            defaultSets = 3,
            defaultReps = "5-10",
            restSeconds = 120,
            instructions = listOf(
                "In den Stütz gehen, Arme gestreckt",
                "Ringe eng am Körper halten",
                "Langsam absenken bis 90° im Ellbogen",
                "Explosiv hochdrücken, Ringe nach außen drehen"
            )
        ),
        Exercise(
            id = "ring_support_hold",
            name = "Ring Support Hold",
            description = "Statischer Stütz an den Ringen. Grundlage für alle Ring-Übungen.",
            muscleGroups = listOf(MuscleGroup.SHOULDERS, MuscleGroup.CORE, MuscleGroup.TRICEPS),
            difficulty = Difficulty.BEGINNER,
            defaultSets = 3,
            defaultReps = "20-30s",
            restSeconds = 60,
            instructions = listOf(
                "In den Stütz springen",
                "Arme durchgestreckt, Ringe am Körper",
                "Schultern nach unten drücken",
                "Position halten, Ringe nach außen drehen"
            )
        ),

        // PULL exercises
        Exercise(
            id = "ring_row",
            name = "Ring Rudern",
            description = "Horizontales Rudern an den Ringen. Perfekt für Rücken und Bizeps.",
            muscleGroups = listOf(MuscleGroup.BACK, MuscleGroup.BICEPS),
            difficulty = Difficulty.BEGINNER,
            defaultSets = 3,
            defaultReps = "8-15",
            restSeconds = 90,
            instructions = listOf(
                "Ringe auf Hüfthöhe einstellen",
                "Unter die Ringe legen, Körper gerade",
                "Zur Brust hochziehen",
                "Ringe am höchsten Punkt nach außen drehen"
            )
        ),
        Exercise(
            id = "ring_pullup",
            name = "Ring Klimmzüge",
            description = "Klimmzüge an den Ringen mit freier Rotation – schont die Gelenke.",
            muscleGroups = listOf(MuscleGroup.BACK, MuscleGroup.BICEPS),
            difficulty = Difficulty.INTERMEDIATE,
            defaultSets = 3,
            defaultReps = "5-10",
            restSeconds = 120,
            instructions = listOf(
                "Ringe greifen, Arme gestreckt hängen",
                "Schulterblätter zusammenziehen",
                "Kinn über die Ringe ziehen",
                "Kontrolliert ablassen"
            )
        ),
        Exercise(
            id = "ring_curl",
            name = "Ring Bizeps Curls",
            description = "Isolierte Bizeps-Übung an den Ringen.",
            muscleGroups = listOf(MuscleGroup.BICEPS),
            difficulty = Difficulty.BEGINNER,
            defaultSets = 3,
            defaultReps = "8-12",
            restSeconds = 60,
            instructions = listOf(
                "Ringe auf Kopfhöhe einstellen",
                "Ringe greifen, Körper zurücklehnen",
                "Nur die Unterarme beugen (Curl-Bewegung)",
                "Kontrolliert zurück"
            )
        ),
        Exercise(
            id = "ring_face_pull",
            name = "Ring Face Pulls",
            description = "Exzellent für hintere Schultern und Haltungskorrektur.",
            muscleGroups = listOf(MuscleGroup.SHOULDERS, MuscleGroup.BACK),
            difficulty = Difficulty.BEGINNER,
            defaultSets = 3,
            defaultReps = "12-15",
            restSeconds = 60,
            instructions = listOf(
                "Ringe auf Kopfhöhe einstellen",
                "Zurücklehnen, Arme gestreckt",
                "Zu den Ohren ziehen, Ellbogen hoch",
                "Schulterblätter zusammendrücken"
            )
        ),

        // CORE exercises
        Exercise(
            id = "ring_ab_rollout",
            name = "Ring Ab Rollout",
            description = "Intensives Core-Training an den Ringen.",
            muscleGroups = listOf(MuscleGroup.CORE),
            difficulty = Difficulty.INTERMEDIATE,
            defaultSets = 3,
            defaultReps = "8-12",
            restSeconds = 90,
            instructions = listOf(
                "Kniend vor den Ringen",
                "Hände in die Ringe, Arme gestreckt",
                "Langsam nach vorne rollen",
                "Core anspannen und zurückziehen"
            )
        ),
        Exercise(
            id = "ring_knee_raise",
            name = "Ring Knieheben",
            description = "Knieheben im Hang an den Ringen.",
            muscleGroups = listOf(MuscleGroup.CORE),
            difficulty = Difficulty.BEGINNER,
            defaultSets = 3,
            defaultReps = "10-15",
            restSeconds = 60,
            instructions = listOf(
                "An den Ringen hängen",
                "Knie zur Brust ziehen",
                "Kontrolliert ablassen",
                "Nicht schwingen"
            )
        ),
        Exercise(
            id = "ring_l_sit",
            name = "Ring L-Sit",
            description = "Statischer L-Sit im Stütz. Intensives Core- und Hüftbeuger-Training.",
            muscleGroups = listOf(MuscleGroup.CORE, MuscleGroup.SHOULDERS),
            difficulty = Difficulty.ADVANCED,
            defaultSets = 3,
            defaultReps = "10-20s",
            restSeconds = 90,
            instructions = listOf(
                "In den Ring-Stütz gehen",
                "Beine gestreckt nach vorne anheben (90°)",
                "Position halten",
                "Schultern unten, Rücken gerade"
            )
        ),

        // ADVANCED
        Exercise(
            id = "ring_muscle_up",
            name = "Ring Muscle Up",
            description = "Die Königsübung: Klimmzug + Dip in einer Bewegung.",
            muscleGroups = listOf(MuscleGroup.FULL_BODY),
            difficulty = Difficulty.ADVANCED,
            defaultSets = 3,
            defaultReps = "3-5",
            restSeconds = 180,
            instructions = listOf(
                "False Grip an den Ringen",
                "Explosiver Klimmzug",
                "Über die Ringe rollen (Transition)",
                "In den Stütz drücken"
            )
        ),
        Exercise(
            id = "ring_pike_pushup",
            name = "Ring Pike Push-Ups",
            description = "Schulter-Übung an den Ringen in Pike-Position.",
            muscleGroups = listOf(MuscleGroup.SHOULDERS, MuscleGroup.TRICEPS),
            difficulty = Difficulty.INTERMEDIATE,
            defaultSets = 3,
            defaultReps = "6-10",
            restSeconds = 90,
            instructions = listOf(
                "Füße auf erhöhter Fläche, Hände in Ringen",
                "Hüfte hoch in Pike-Position",
                "Kopf zwischen den Ringen absenken",
                "Hochdrücken"
            )
        )
    )

    val workouts = listOf(
        Workout(
            id = "push_day",
            name = "Push Tag",
            description = "Drückende Bewegungen: Brust, Schultern, Trizeps",
            category = WorkoutCategory.PUSH,
            exercises = listOf(
                WorkoutExercise(getExercise("ring_support_hold"), 3, "20-30s", 60),
                WorkoutExercise(getExercise("ring_dip"), 4, "6-10", 120),
                WorkoutExercise(getExercise("ring_pushup"), 3, "10-15", 90),
                WorkoutExercise(getExercise("ring_pike_pushup"), 3, "6-10", 90)
            )
        ),
        Workout(
            id = "pull_day",
            name = "Pull Tag",
            description = "Ziehende Bewegungen: Rücken, Bizeps",
            category = WorkoutCategory.PULL,
            exercises = listOf(
                WorkoutExercise(getExercise("ring_pullup"), 4, "5-8", 120),
                WorkoutExercise(getExercise("ring_row"), 3, "10-15", 90),
                WorkoutExercise(getExercise("ring_face_pull"), 3, "12-15", 60),
                WorkoutExercise(getExercise("ring_curl"), 3, "8-12", 60)
            )
        ),
        Workout(
            id = "full_body",
            name = "Ganzkörper",
            description = "Alle Muskelgruppen in einem Workout",
            category = WorkoutCategory.FULL_BODY,
            exercises = listOf(
                WorkoutExercise(getExercise("ring_pullup"), 3, "5-8", 120),
                WorkoutExercise(getExercise("ring_dip"), 3, "6-10", 120),
                WorkoutExercise(getExercise("ring_row"), 3, "10-12", 90),
                WorkoutExercise(getExercise("ring_pushup"), 3, "10-15", 90),
                WorkoutExercise(getExercise("ring_knee_raise"), 3, "10-15", 60)
            )
        ),
        Workout(
            id = "core_day",
            name = "Core Training",
            description = "Intensives Core- und Bauch-Training",
            category = WorkoutCategory.CORE,
            exercises = listOf(
                WorkoutExercise(getExercise("ring_knee_raise"), 3, "12-15", 60),
                WorkoutExercise(getExercise("ring_ab_rollout"), 3, "8-12", 90),
                WorkoutExercise(getExercise("ring_l_sit"), 3, "10-20s", 90),
                WorkoutExercise(getExercise("ring_support_hold"), 3, "30s", 60)
            )
        ),
        Workout(
            id = "beginner",
            name = "Einsteiger Programm",
            description = "Perfekt für den Einstieg ins Ring-Training",
            category = WorkoutCategory.FULL_BODY,
            exercises = listOf(
                WorkoutExercise(getExercise("ring_support_hold"), 3, "15-20s", 60),
                WorkoutExercise(getExercise("ring_row"), 3, "8-12", 90),
                WorkoutExercise(getExercise("ring_pushup"), 3, "5-10", 90),
                WorkoutExercise(getExercise("ring_knee_raise"), 3, "8-12", 60),
                WorkoutExercise(getExercise("ring_face_pull"), 3, "10-12", 60)
            )
        )
    )

    private fun getExercise(id: String): Exercise {
        return exercises.first { it.id == id }
    }
}
