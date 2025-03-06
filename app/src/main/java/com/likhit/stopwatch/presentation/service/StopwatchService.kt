package com.likhit.stopwatch.presentation.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.os.SystemClock
import androidx.core.app.NotificationCompat
import com.likhit.stopwatch.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.toJavaDuration
import kotlin.time.toKotlinDuration

class StopwatchService: Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val binder = StopwatchBinder()

    private var _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning

    private var _time = MutableStateFlow(Duration.ZERO)
    val time: StateFlow<Duration> = _time

    private var lastTimestamp = 0L
    private val notificationId = 1
    private val channelId = "stopwatch_channel"

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = createNotification()
        startForeground(notificationId, notification)

        when(intent?.action){
            Actions.START.toString() -> start()
            Actions.PAUSE.toString() -> pause()
            Actions.RESET.toString() -> reset()
            Actions.STOP.toString() -> {
                stopSelf()
            }
        }
        return START_STICKY
    }

    override fun onBind(p0: Intent): IBinder {
        return binder
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    fun start(){
        if(_isRunning.value) {
            return
        }
        lastTimestamp = SystemClock.elapsedRealtime()
        _isRunning.value = true


        serviceScope.launch {
            while (_isRunning.value){
                val now = SystemClock.elapsedRealtime()
                val elapsed = now - lastTimestamp
                lastTimestamp = now

                if (!_isRunning.value) break

                _time.value = _time.value.plus(java.time.Duration.ofMillis(elapsed).toKotlinDuration())
                updateNotification()
                delay(1000L)
            }
        }
    }

    fun pause(){
        _isRunning.value = false
        updateNotification()
    }

    fun reset(){
        _isRunning.value = false
        _time.value = Duration.ZERO
        updateNotification()
    }

    private fun updateNotification(){
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(
            notificationId,
            createNotification()
        )
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            channelId,
            "Stopwatch Service",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Keeps the stopwatch running in the background"
        }
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun createNotification(): Notification {
        val isRunning = _isRunning.value
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Stopwatch")
            .setContentText(formatTime())
            .setSmallIcon(R.drawable.stopwatch_service_logo)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(createPendingIntent())
            .addAction(
                R.drawable.ic_launcher_foreground,
                if(isRunning) "Pause" else "Play",
                createPendingIntent(
                    if(isRunning) Actions.PAUSE else Actions.START
                )
            )
            .addAction(
                R.drawable.ic_launcher_foreground,
                if(!isRunning) "Reset" else "",
                createPendingIntent(
                    Actions.RESET
                )
            )
            .build()
    }

    private fun formatTime(): String{
        val totalMilliseconds = _time.value.toJavaDuration().toMillis()
        val totalSeconds = totalMilliseconds / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        return "${hours.pad()}:${minutes.pad()}:${seconds.pad()}"
    }

    private fun Long.pad() = toString().padStart(2, '0')

    private fun createPendingIntent(): PendingIntent{
        val intent = packageManager.getLaunchIntentForPackage(packageName)?.apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        return PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun createPendingIntent(action: Actions): PendingIntent{
        val intent = Intent(this, StopwatchService::class.java).apply {
            this.action = action.toString()
        }

        return PendingIntent.getService(
            this,
            action.ordinal,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    inner class StopwatchBinder: Binder(){
        fun getService(): StopwatchService = this@StopwatchService
    }
}