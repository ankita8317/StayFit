package com.example.stayfit.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WorkoutProgressDao {
    @Insert
    suspend fun insert(progress: WorkoutProgress)

    @Query("SELECT * FROM workout_progress WHERE date >= :fromDate ORDER BY date DESC")
    suspend fun getProgressSince(fromDate: Long): List<WorkoutProgress>
} 