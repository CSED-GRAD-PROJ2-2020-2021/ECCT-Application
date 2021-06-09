package com.desireProj.ble_sdk.tools

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.desireProj.ble_sdk.MainActivity
import com.desireProj.ble_sdk.R
import com.desireProj.ble_sdk.model.Utilities
import kotlinx.coroutines.*


class BleForegroundService(): Service() {
    private var isServiceStarted = false
    var engine: Engine
    init {
        engine = Engine
    }
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
                launch(Dispatchers.Default) {
//                    Utilities.context = MyApplication.applicationContext()
                    Log.e("Foreground Default", "utilities context : " + Utilities.context)
                    engine.generateNewKey()
                    engine.clearEbidMap()
                    engine.startScanning()
                    engine.startAdvertising()
                }
                delay(1 *6 * 1000)
                engine.stopScanning()
            }
            log("End of the loop for the service")
        }

//        GlobalScope.launch(Dispatchers.Default) {
//            while (isServiceStarted) {
//                engine.startAdvertising()
////                delay(2 * 1000)
//            }
//            log("End of the loop for the advertising")
//        }

        // delete expired pets from database
        GlobalScope.launch(Dispatchers.IO) {
            if (isServiceStarted) {
                engine.removeExpiredPetsFromDatabase()
            }
            while (isServiceStarted) {
//                Utilities.context = MyApplication.applicationContext()
                Log.e("Foreground IO", "utilities context : " + Utilities.context)
                engine.sendPetsToDatabase()
                engine.updateDatabasePassword()
                delay(1 *2 * 1000)
//                var list = engine.dataBaseHandler.getRtlItems()
//                Log.e("Main Activity", "rtl table size: " + list.size)
//                for (rtl in list) {
//                    Log.e("Main Activity", "rtl item : " + rtl.pet + " date: " + rtl.day)
//                }
//                log("Database cleaned")
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
    fun createNotification():Notification{
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