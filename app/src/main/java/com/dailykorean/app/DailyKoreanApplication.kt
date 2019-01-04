package com.dailykorean.app

import android.app.Application
import com.facebook.stetho.Stetho
import android.app.PendingIntent
import android.content.Intent
import com.dailykorean.app.notification.NotificationReceiver
import android.app.AlarmManager
import android.content.Context
import java.util.*


/**
 * Created by musooff on 01/01/2019.
 */

class DailyKoreanApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)

        setRecurringAlarm(this)
    }

    private fun setRecurringAlarm(context: Context) {

        val updateTime = Calendar.getInstance()
        updateTime.set(Calendar.HOUR_OF_DAY, 8)
        updateTime.set(Calendar.MINUTE, 30)

        val downloader = Intent(context, NotificationReceiver::class.java)
        val recurringDownload = PendingIntent.getBroadcast(context, 0, downloader, PendingIntent.FLAG_CANCEL_CURRENT)
        val alarms = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarms.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                updateTime.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                recurringDownload)
    }
}