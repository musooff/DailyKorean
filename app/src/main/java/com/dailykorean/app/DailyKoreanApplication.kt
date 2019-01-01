package com.dailykorean.app

import android.app.Application
import com.facebook.stetho.Stetho

/**
 * Created by musooff on 01/01/2019.
 */

class DailyKoreanApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
    }
}