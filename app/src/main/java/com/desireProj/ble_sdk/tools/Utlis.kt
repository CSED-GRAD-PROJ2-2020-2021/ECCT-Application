package com.desireProj.ble_sdk.tools

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log

lateinit var alarmMgr: AlarmManager
lateinit var pIntent : PendingIntent
fun log(msg: String) {
    Log.d("ENDLESS-SERVICE", msg)
}
fun setAlarm(context: Context?) {

    // Intent to start the Broadcast Receiver
    val broadcastIntent = Intent(context
        , AlarmBroadcastReciever::class.java)

    pIntent = PendingIntent.getBroadcast(
        context,
        0,
        broadcastIntent,
        0
    )

    // Setting up AlarmManager
    alarmMgr = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager


    alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime()
        ,(3 * 1000), pIntent);
    log(System.currentTimeMillis().toString())
}
fun cancelAlarm(){
    alarmMgr.cancel(pIntent)
}