package com.dailykorean.app

import android.app.Application
import com.facebook.stetho.Stetho
import com.dailykorean.app.notification.AlarmScheduler
import com.google.android.gms.ads.MobileAds

/**
 * Created by musooff on 01/01/2019.
 */

class DailyKoreanApplication : Application() {

    private lateinit var appPreference: AppPreference

    override fun onCreate() {
        super.onCreate()

        Stetho.initializeWithDefaults(this)

        MobileAds.initialize(this, this.getString(R.string.AppID))

        appPreference = AppPreference.getInstance(this)

        if (appPreference.isNotificationEnabled() && !appPreference.isAlarmSet()){
            AlarmScheduler(this).updateAlarmSettings(true)
        }
    }
}