package com.example.stayfit.data

import java.util.Date

data class WorkoutSession(
    val date: Long, // Store as timestamp for easier serialization
    val durationSeconds: Int,
    val exercisesCompleted: Int
) 