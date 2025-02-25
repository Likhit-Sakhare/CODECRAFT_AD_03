package com.likhit.stopwatch.presentation.stopwatch

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class StopwatchViewModel @Inject constructor(): ViewModel() {
    private var time: Duration = Duration.ZERO
    private var timer: Timer = Timer()

    var milliseconds by mutableStateOf("00")
    var seconds by mutableStateOf("00")
    var minutes by mutableStateOf("00")
    var hours by mutableStateOf("00")
    var isPlaying by mutableStateOf(false)

    fun start(){
        timer = fixedRateTimer(
            initialDelay = 10L,
            period = 10L
        ){
            time = time.plus(10.milliseconds)
            updateTimeStates()
        }
        isPlaying = true
    }

    private fun updateTimeStates(){
        val totalMilliseconds = time.inWholeMilliseconds
        val totalSeconds = totalMilliseconds / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        val milliseconds = (totalMilliseconds % 1000) / 10

        this@StopwatchViewModel.hours = hours.toInt().pad()
        this@StopwatchViewModel.minutes = minutes.toInt().pad()
        this@StopwatchViewModel.seconds = seconds.toInt().pad()
        this@StopwatchViewModel.milliseconds = milliseconds.toInt().pad()
    }

    private fun Int.pad(): String{
        return this.toString().padStart(2, '0')
    }

    fun pause(){
        timer.cancel()
        isPlaying = false
    }

    fun reset(){
        pause()
        time = Duration.ZERO
        updateTimeStates()
    }
}