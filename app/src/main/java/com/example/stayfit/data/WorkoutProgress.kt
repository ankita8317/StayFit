package com.example.stayfit.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_progress")
data class WorkoutProgress(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Long, // Store as epoch millis
    val durationMinutes: Int,
    val exercisesCompleted: Int
) 