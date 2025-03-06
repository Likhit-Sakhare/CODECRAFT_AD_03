package com.likhit.stopwatch.presentation.stopwatch

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.likhit.stopwatch.presentation.service.Actions
import com.likhit.stopwatch.presentation.service.StopwatchService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.toJavaDuration

@HiltViewModel
class StopwatchViewModel @Inject constructor(
    private val application: Application
): ViewModel() {

    private var serviceConnection: ServiceConnection? = null
    private var stopwatchService: StopwatchService? = null
    private var isBound = false

    var seconds by mutableStateOf("00")
        private set
    var minutes by mutableStateOf("00")
        private set
    var hours by mutableStateOf("00")
        private set
    var isPlaying by mutableStateOf(false)
        private set

    init {
        bindService()
    }

    fun start(){
        isPlaying = true
        stopwatchService?.start()?: startService(Actions.START)
    }

    fun pause(){
        isPlaying = false
        stopwatchService?.pause()?: startService(Actions.PAUSE)
    }

    fun reset(){
        isPlaying = false
        stopwatchService?.reset()?: startService(Actions.RESET)
    }

    private fun bindService(){
        if(isBound){
            return
        }
        val serviceConnection = object : ServiceConnection{
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val binder = service as StopwatchService.StopwatchBinder
                stopwatchService = binder.getService()
                isBound = true

                viewModelScope.launch {
                    stopwatchService?.isRunning?.collectLatest { running ->
                        isPlaying = running
                    }
                }

                viewModelScope.launch {
                    stopwatchService?.time?.collectLatest { time ->
                        updateTimeStates(time.toJavaDuration())
                    }
                }
            }

            override fun onServiceDisconnected(p0: ComponentName?) {
                isBound = false
                stopwatchService = null
            }
        }

        this.serviceConnection = serviceConnection

        Intent(
            application,
            StopwatchService::class.java
        ).also { intent ->
            application.bindService(
                intent,
                serviceConnection,
                Context.BIND_AUTO_CREATE
            )
        }
    }

    private fun startService(action: Actions){
        Intent(
            application,
            StopwatchService::class.java
        ).also { intent ->
            intent.action = action.toString()
            application.startForegroundService(intent)
        }
        if(!isBound){
            bindService()
        }
    }

    private fun updateTimeStates(time: java.time.Duration){
        val totalMilliseconds = time.toMillis()
        val totalSeconds = totalMilliseconds / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        this@StopwatchViewModel.hours = hours.toInt().pad()
        this@StopwatchViewModel.minutes = minutes.toInt().pad()
        this@StopwatchViewModel.seconds = seconds.toInt().pad()
    }

    private fun Int.pad(): String{
        return this.toString().padStart(2, '0')
    }

    override fun onCleared() {
        super.onCleared()
        serviceConnection?.let {
            application.unbindService(it)
            isBound = false
        }
    }
}