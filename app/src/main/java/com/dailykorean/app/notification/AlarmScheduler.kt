package com.dailykorean.app.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.dailykorean.app.AppPreference
import java.util.*

/**
 * Created by musooff on 05/01/2019.
 */

class AlarmScheduler(context: Context) {

    private val appPreference = AppPreference.getInstance(context)

    private val downloader = Intent(context, AlarmReceiver::class.java)
    private val recurringDownload = PendingIntent.getBroadcast(context, 0, downloader, PendingIntent.FLAG_CANCEL_CURRENT)!!
    private val alarms = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun updateAlarmSettings(enable: Boolean){
        if (enable){
            enableAlarm()
            appPreference.setAlarmSet(true)
        }
        else{
            disableAlarm()
            appPreference.setAlarmSet(false)
        }
    }

    private fun enableAlarm(){
        val updateTime = Calendar.getInstance()
        updateTime.set(Calendar.HOUR_OF_DAY, 8)
        updateTime.set(Calendar.MINUTE, 30)

        alarms.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                updateTime.timeInMillis + AlarmManager.INTERVAL_DAY,
                AlarmManager.INTERVAL_DAY,
                recurringDownload)
    }

    private fun disableAlarm(){
        alarms.cancel(recurringDownload)
    }
}