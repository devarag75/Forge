
package com.devarag.forge.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FocusViewModel : ViewModel() {

    private var timerJob: Job? = null

    var selectedDurationSeconds by mutableIntStateOf(25 * 60)
        private set

    var remainingSeconds by mutableIntStateOf(25 * 60)
        private set

    var isRunning by mutableStateOf(false)
        private set

    val minutes: Int
        get() = remainingSeconds / 60

    val seconds: Int
        get() = remainingSeconds % 60

    val selectedHours: Int
        get() = selectedDurationSeconds / 3600

    val selectedMinutes: Int
        get() = (selectedDurationSeconds % 3600) / 60

    fun setDuration(hours: Int, minutes: Int): Boolean {

        if (isRunning) return false

        if (hours !in 0..23 || minutes !in 0..59) {
            return false
        }

        val totalSeconds = hours * 3600 + minutes * 60

        if (totalSeconds <= 0) {
            return false
        }

        timerJob?.cancel()
        timerJob = null

        selectedDurationSeconds = totalSeconds
        remainingSeconds = totalSeconds

        return true
    }

    fun start() {

        if (isRunning || remainingSeconds <= 0) return

        isRunning = true

        timerJob?.cancel()

        timerJob = viewModelScope.launch {

            while (isRunning && remainingSeconds > 0) {

                delay(1000)

                if (isRunning && remainingSeconds > 0) {
                    remainingSeconds--
                }
            }

            if (remainingSeconds == 0) {
                isRunning = false
            }
        }
    }

    fun pause() {
        isRunning = false
        timerJob?.cancel()
        timerJob = null
    }

    fun reset() {
        pause()
        remainingSeconds = selectedDurationSeconds
    }

    override fun onCleared() {
        timerJob?.cancel()
        super.onCleared()
    }
}
