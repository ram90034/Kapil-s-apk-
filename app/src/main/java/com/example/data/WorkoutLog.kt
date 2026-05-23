package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_logs")
data class WorkoutLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val exerciseId: String,
    val exerciseName: String,
    val category: String,
    val weight: Double,
    val reps: Int,
    val sets: Int,
    val notes: String = "",
    val dateMillis: Long = System.currentTimeMillis()
)

@Entity(tableName = "body_weight_logs")
data class BodyWeightLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val weight: Double,
    val dateMillis: Long = System.currentTimeMillis()
)
