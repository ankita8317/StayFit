package com.example.stayfit.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WorkoutProgress::class], version = 1)
abstract class StayFitDatabase : RoomDatabase() {
    abstract fun workoutProgressDao(): WorkoutProgressDao
} 