package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutLogDao {
    @Query("SELECT * FROM workout_logs ORDER BY dateMillis DESC")
    fun getAllWorkoutLogs(): Flow<List<WorkoutLog>>

    @Query("SELECT * FROM workout_logs WHERE exerciseId = :exerciseId ORDER BY dateMillis DESC")
    fun getLogsForExercise(exerciseId: String): Flow<List<WorkoutLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutLog(log: WorkoutLog)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutLogs(logs: List<WorkoutLog>)

    @Query("DELETE FROM workout_logs WHERE id = :id")
    suspend fun deleteWorkoutLogById(id: Int)

    @Query("SELECT * FROM body_weight_logs ORDER BY dateMillis DESC")
    fun getAllBodyWeightLogs(): Flow<List<BodyWeightLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBodyWeightLog(log: BodyWeightLog)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBodyWeightLogs(logs: List<BodyWeightLog>)

    @Query("DELETE FROM body_weight_logs WHERE id = :id")
    suspend fun deleteBodyWeightLogById(id: Int)
}
