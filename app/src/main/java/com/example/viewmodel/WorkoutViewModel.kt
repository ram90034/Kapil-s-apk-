package com.example.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class GoogleUser(
    val email: String,
    val displayName: String,
    val photoUrl: String? = null
)

class WorkoutViewModel(
    private val repository: WorkoutRepository,
    private val context: Context
) : ViewModel() {

    private val prefs = context.getSharedPreferences("user_auth_prefs", Context.MODE_PRIVATE)

    // Authenticated Google User Session
    private val _currentUser = MutableStateFlow<GoogleUser?>(null)
    val currentUser: StateFlow<GoogleUser?> = _currentUser.asStateFlow()

    // Sync States
    private val _syncInProgress = MutableStateFlow(false)
    val syncInProgress: StateFlow<Boolean> = _syncInProgress.asStateFlow()

    private val _lastSyncTime = MutableStateFlow<Long?>(null)
    val lastSyncTime: StateFlow<Long?> = _lastSyncTime.asStateFlow()

    private val _syncErrorMessage = MutableStateFlow<String?>(null)
    val syncErrorMessage: StateFlow<String?> = _syncErrorMessage.asStateFlow()

    // Selected Muscle Category for filtering Exercises
    private val _selectedCategory = MutableStateFlow(ExerciseData.categories.first())
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    // Observed lists reactively loaded from database
    val workoutLogs: StateFlow<List<WorkoutLog>> = repository.allWorkoutLogs
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val bodyWeightLogs: StateFlow<List<BodyWeightLog>> = repository.allBodyWeightLogs
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        // Hydrate configuration on load
        val email = prefs.getString("user_email", null)
        val name = prefs.getString("user_name", null)
        val photo = prefs.getString("user_photo", null)
        if (email != null && name != null) {
            _currentUser.value = GoogleUser(email, name, photo)
        }

        val lastSync = prefs.getLong("last_sync_time", 0L)
        if (lastSync > 0L) {
            _lastSyncTime.value = lastSync
        }
    }

    fun signInUser(email: String, name: String, photoUrl: String?) {
        prefs.edit().apply {
            putString("user_email", email)
            putString("user_name", name)
            putString("user_photo", photoUrl)
            apply()
        }
        _currentUser.value = GoogleUser(email, name, photoUrl)
    }

    fun signOutUser() {
        prefs.edit().clear().apply()
        _currentUser.value = null
        _lastSyncTime.value = null
    }

    // Backup current Room database logs to Secure Cloud
    fun backupFitnessData() {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            _syncInProgress.value = true
            _syncErrorMessage.value = null
            
            val payload = SyncPayload(
                email = user.email,
                name = user.displayName,
                workouts = workoutLogs.value,
                weights = bodyWeightLogs.value
            )
            
            val success = CloudSyncService.backupToCloud(payload)
            _syncInProgress.value = false
            
            if (success) {
                val now = System.currentTimeMillis()
                prefs.edit().putLong("last_sync_time", now).apply()
                _lastSyncTime.value = now
            } else {
                _syncErrorMessage.value = "Backup failed. Confirm internet connection is online."
            }
        }
    }

    // Restore logs from Secure Cloud and merge into Room database
    fun restoreFitnessData() {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            _syncInProgress.value = true
            _syncErrorMessage.value = null
            
            val payload = CloudSyncService.restoreFromCloud(user.email)
            _syncInProgress.value = false
            
            if (payload != null) {
                // Bulk insert restored payload list to local database
                if (payload.workouts.isNotEmpty()) {
                    repository.insertWorkoutLogs(payload.workouts)
                }
                if (payload.weights.isNotEmpty()) {
                    repository.insertBodyWeightLogs(payload.weights)
                }
                
                val now = System.currentTimeMillis()
                prefs.edit().putLong("last_sync_time", now).apply()
                _lastSyncTime.value = now
            } else {
                _syncErrorMessage.value = "No remote backup data found on cloud for this account."
            }
        }
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    // Insert logged workouts
    fun logWorkout(
        exerciseId: String,
        exerciseName: String,
        category: String,
        weight: Double,
        reps: Int,
        sets: Int,
        notes: String = ""
    ) {
        viewModelScope.launch {
            val log = WorkoutLog(
                exerciseId = exerciseId,
                exerciseName = exerciseName,
                category = category,
                weight = weight,
                reps = reps,
                sets = sets,
                notes = notes
            )
            repository.insertWorkoutLog(log)
        }
    }

    // Insert body weight log
    fun logBodyWeight(weight: Double) {
        viewModelScope.launch {
            val log = BodyWeightLog(weight = weight)
            repository.insertBodyWeightLog(log)
        }
    }

    // Delete logged workout
    fun deleteWorkoutLog(id: Int) {
        viewModelScope.launch {
            repository.deleteWorkoutLogById(id)
        }
    }

    // Delete body weight
    fun deleteBodyWeightLog(id: Int) {
        viewModelScope.launch {
            repository.deleteBodyWeightLogById(id)
        }
    }
}

// Custom ViewModel Factory to support simple constructor injection easily without Hilt!
class WorkoutViewModelFactory(
    private val repository: WorkoutRepository,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WorkoutViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WorkoutViewModel(repository, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
