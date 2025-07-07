package com.example.stayfit.data

data class Progress(
    val date: Long, // Store as timestamp (milliseconds since epoch)
    val durationMinutes: Int,
    val exercisesCompleted: Int
) 