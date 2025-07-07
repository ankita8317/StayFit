package com.example.stayfit.data

data class Exercise(
    val name: String,
    val durationInSeconds: Int,
    val imageResourceId: Int,
    val description: String = ""
) 