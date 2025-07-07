package com.example.stayfit.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stayfit.data.Exercise
import com.example.stayfit.data.WorkoutData
import com.example.stayfit.data.WorkoutSession
import com.example.stayfit.data.StayFitDatabase
import com.example.stayfit.data.WorkoutProgress
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.room.Room
import java.util.Calendar

class WorkoutViewModel(application: Application) : AndroidViewModel(application) {

    private val _currentExercise = MutableStateFlow<Exercise?>(null)
    val currentExercise = _currentExercise.asStateFlow()

    private val _nextExercise = MutableStateFlow<Exercise?>(null)
    val nextExercise = _nextExercise.asStateFlow()

    private val _timeLeft = MutableStateFlow(0)
    val timeLeft = _timeLeft.asStateFlow()

    private val _workoutInProgress = MutableStateFlow(false)
    val workoutInProgress = _workoutInProgress.asStateFlow()

    private val _isPaused = MutableStateFlow(false)
    val isPaused = _isPaused.asStateFlow()

    private var timerJob: Job? = null
    private var currentCategory: String = "Classic"
    private var workoutList: List<Exercise> = WorkoutData.workoutsByCategory[currentCategory] ?: emptyList()
    private var currentExerciseIndex = 0
    private var workoutStartTime: Long = 0
    private var exercisesCompleted: Int = 0

    private val db = Room.databaseBuilder(
        application,
        StayFitDatabase::class.java, "stayfit-db"
    ).build()
    private val progressDao = db.workoutProgressDao()

    private val _progressLast7Days = MutableLiveData<List<WorkoutProgress>>()
    val progressLast7Days: LiveData<List<WorkoutProgress>> = _progressLast7Days

    private val _progressLast30Days = MutableLiveData<List<WorkoutProgress>>()
    val progressLast30Days: LiveData<List<WorkoutProgress>> = _progressLast30Days

    val currentWorkoutList: List<Exercise>
        get() = workoutList

    val currentNonRestExercises: List<Exercise>
        get() = workoutList.filter { !it.name.equals("Rest", ignoreCase = true) }

    fun setCategory(category: String) {
        currentCategory = category
        workoutList = WorkoutData.workoutsByCategory[category] ?: emptyList()
    }

    fun startWorkout() {
        if (_workoutInProgress.value) return

        _workoutInProgress.value = true
        _isPaused.value = false
        currentExerciseIndex = 0
        workoutStartTime = System.currentTimeMillis()
        exercisesCompleted = 0
        startNextExercise()
    }

    fun togglePause() {
        _isPaused.value = !_isPaused.value
        if (_isPaused.value) {
            timerJob?.cancel()
        } else {
            resumeTimer()
        }
    }

    private fun startNextExercise() {
        timerJob?.cancel() // Cancel any existing timer
        if (currentExerciseIndex >= workoutList.size) {
            endWorkout()
            return
        }

        val exercise = workoutList[currentExerciseIndex]
        _currentExercise.value = exercise
        _timeLeft.value = exercise.durationInSeconds

        if (!exercise.name.equals("Rest", ignoreCase = true)) {
            exercisesCompleted++
        }

        if (currentExerciseIndex + 1 < workoutList.size) {
            _nextExercise.value = workoutList[currentExerciseIndex + 1]
        } else {
            _nextExercise.value = null
        }

        resumeTimer()
    }

    private fun resumeTimer() {
        timerJob = viewModelScope.launch {
            while (_timeLeft.value > 0) {
                delay(1000)
                _timeLeft.value--
            }
            currentExerciseIndex++
            startNextExercise()
        }
    }

    fun endWorkout(context: Context? = null) {
        _workoutInProgress.value = false
        timerJob?.cancel()
        timerJob = null
        _currentExercise.value = null
        _nextExercise.value = null
        _isPaused.value = false

        val durationSeconds = ((System.currentTimeMillis() - workoutStartTime) / 1000).toInt()
        val durationMinutes = (durationSeconds + 59) / 60 // round up to nearest minute
        saveWorkoutProgress(durationMinutes, exercisesCompleted)

        // Save session if context is provided
        context?.let {
            saveWorkoutSession(it, durationSeconds, exercisesCompleted)
        }
    }

    fun skipToNextExercise() {
        timerJob?.cancel()
        currentExerciseIndex++
        startNextExercise()
    }

    fun saveWorkoutSession(context: Context, durationSeconds: Int, exercisesCompleted: Int) {
        val prefs = context.getSharedPreferences("progress", Context.MODE_PRIVATE)
        val sessionsJson = prefs.getString("sessions", "[]")
        val gson = Gson()
        val type = object : TypeToken<MutableList<WorkoutSession>>() {}.type
        val sessionList: MutableList<WorkoutSession> = gson.fromJson(sessionsJson, type) ?: mutableListOf()
        sessionList.add(WorkoutSession(System.currentTimeMillis(), durationSeconds, exercisesCompleted))
        prefs.edit().putString("sessions", gson.toJson(sessionList)).apply()
    }

    fun saveWorkoutProgress(durationMinutes: Int, exercisesCompleted: Int) {
        viewModelScope.launch {
            val progress = WorkoutProgress(
                date = System.currentTimeMillis(),
                durationMinutes = durationMinutes,
                exercisesCompleted = exercisesCompleted
            )
            progressDao.insert(progress)
            fetchProgressLast7Days()
        }
    }

    fun fetchProgressLast7Days() {
        viewModelScope.launch {
            val sevenDaysAgo = System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000
            val progress = progressDao.getProgressSince(sevenDaysAgo)
            _progressLast7Days.postValue(progress)
        }
    }

    fun fetchProgressLast30Days() {
        viewModelScope.launch {
            val thirtyDaysAgo = System.currentTimeMillis() - 30 * 24 * 60 * 60 * 1000
            val progress = progressDao.getProgressSince(thirtyDaysAgo)
            _progressLast30Days.postValue(progress)
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
} 