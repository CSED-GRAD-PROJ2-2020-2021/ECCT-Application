package com.ecct.protocol.tools

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.ecct.demo.MainActivity
import com.ecct.protocol.R
import com.ecct.protocol.model.Utilities
import kotlinx.coroutines.*

private const val EPOCH_LENGTH_MINUTES: Long = 5 *60 * 1000
private const val SCAN_PERIOD_MINUTES: Long = 6 * 1000
private const val SCAN_REST_PERIOD_MINUTES: Long = 2 * 1000

class BleForegroundService(): Service() {
    private var isServiceStarted = false
    var engine: Engine = Engine

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStart(intent: Intent?, startId: Int) {
        super.onStart(intent, startId)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log("onStartCommand executed with startId: $startId")
        if (intent != null) {
            val action = intent.action
            log("using an intent with action $action")
            when (action) {
                Actions.START.name -> startService()
                Actions.STOP.name -> stopService()
                else -> log("This should never happen. No action in the received intent")
            }
        } else {
            log(
                "with a null intent. It has been probably restarted by the system."
            )
        }
        // by returning this we make sure the service is restarted if the system kills the service
        return START_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun startService() {
        if (isServiceStarted) return
        log("Starting the foreground service task")
        Toast.makeText(this, "Service starting its task", Toast.LENGTH_SHORT).show()
        isServiceStarted = true
        setServiceState(this, ServiceState.STARTED)

        // thread to update the status per epoch
        GlobalScope.launch(Dispatchers.Default) {
            while (isServiceStarted) {
                Log.e("Foreground Default", "utilities context : " + Utilities.context)
                engine.generateNewKey()
                engine.clearEbidMap()
                delay(EPOCH_LENGTH_MINUTES)
            }
            log("End of the loop for the service")
        }

        GlobalScope.launch(Dispatchers.Default) {
            while (isServiceStarted) {
                engine.startAdvertising()   // takes 16 seconds
//                delay(2 * 1000)
            }
            log("End of the loop for the advertising")
        }

        GlobalScope.launch(Dispatchers.Default) {
            while (isServiceStarted) {
                engine.startScanning()
                delay(SCAN_PERIOD_MINUTES)
                engine.stopScanning()
                delay(SCAN_REST_PERIOD_MINUTES)
            }
            log("End of the loop for the advertising")
        }

        // delete expired pets from database
        GlobalScope.launch(Dispatchers.IO) {
            if (isServiceStarted) {
                engine.removeExpiredPetsFromDatabase()
            }
            while (isServiceStarted) {
                Log.e("Foreground IO", "utilities context : " + Utilities.context)
                engine.sendPetsToDatabase() // send collected pets to database and clear pets map
                engine.updateDatabasePassword()
                delay(EPOCH_LENGTH_MINUTES)
            }
        }

    }

    override fun onCreate() {
        super.onCreate()
        log("The service has been created".toUpperCase())
        var notification = createNotification()
        startForeground(1, notification)
    }

    private fun stopService() {
        stopForeground(true)
        stopSelf()
        isServiceStarted = false
        setServiceState(this, ServiceState.STOPPED)
    }

    override fun onDestroy() {
        super.onDestroy()
        log("The service has been destroyed".toUpperCase())
        Toast.makeText(this, "Service destroyed", Toast.LENGTH_SHORT).show()
    }

    private fun createNotification():Notification{
        val notificationChannelId = "ENDLESS SERVICE CHANNEL"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager;
            val channel = NotificationChannel(
                notificationChannelId,
                "Endless Service notifications channel",
                NotificationManager.IMPORTANCE_HIGH
            ).let {
                it.description = "Endless Service channel"
                it.enableLights(true)
                it.lightColor = Color.RED
                it.enableVibration(true)
                it.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                it
            }
            notificationManager.createNotificationChannel(channel)
        }
        val pendingIntent: PendingIntent = Intent(this, MainActivity::class.java).let { notificationIntent ->
            PendingIntent.getActivity(this, 0, notificationIntent, 0)
        }
        val builder: Notification.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) Notification.Builder(
            this,
            notificationChannelId
        ) else Notification.Builder(this)
        return builder
            .setContentTitle("Endless Service")
            .setContentText("This is your favorite endless service working")
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setTicker("Ticker text")
            .setPriority(Notification.PRIORITY_HIGH) // for under android 26 compatibility
            .build()

    }
}