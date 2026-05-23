package com.example.data

import kotlinx.coroutines.flow.Flow

class WorkoutRepository(private val workoutLogDao: WorkoutLogDao) {

    val allWorkoutLogs: Flow<List<WorkoutLog>> = workoutLogDao.getAllWorkoutLogs()
    
    val allBodyWeightLogs: Flow<List<BodyWeightLog>> = workoutLogDao.getAllBodyWeightLogs()

    fun getLogsForExercise(exerciseId: String): Flow<List<WorkoutLog>> {
        return workoutLogDao.getLogsForExercise(exerciseId)
    }

    suspend fun insertWorkoutLog(log: WorkoutLog) {
        workoutLogDao.insertWorkoutLog(log)
    }

    suspend fun insertWorkoutLogs(logs: List<WorkoutLog>) {
        workoutLogDao.insertWorkoutLogs(logs)
    }

    suspend fun deleteWorkoutLogById(id: Int) {
        workoutLogDao.deleteWorkoutLogById(id)
    }

    suspend fun insertBodyWeightLog(log: BodyWeightLog) {
        workoutLogDao.insertBodyWeightLog(log)
    }

    suspend fun insertBodyWeightLogs(logs: List<BodyWeightLog>) {
        workoutLogDao.insertBodyWeightLogs(logs)
    }

    suspend fun deleteBodyWeightLogById(id: Int) {
        workoutLogDao.deleteBodyWeightLogById(id)
    }
}
