package com.dailykorean.app

import android.content.Context
import android.preference.PreferenceManager

/**
 * Created by musooff on 05/01/2019.
 */

class AppPreference(context: Context) {

    companion object {

        private var INSTANCE: AppPreference? = null

        fun getInstance(context: Context) =
                INSTANCE ?: AppPreference(context)
                        .also {
                            INSTANCE = it
                        }

        private const val IS_ALARM_SET = "is_alarm_set"
        private const val NOTIFICATION_NEW_EXPRESSION = "notifications_new_message"
        private const val NOTIFICATION_NEW_EXPRESSION_RINGTONE = "notifications_new_message_ringtone"
        private const val DEFAULT_NOTIFICATION_NEW_EXPRESSION_RINGTONE = "content://settings/system/notification_sound"
    }

    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val editor = sharedPreferences.edit()

    fun isAlarmSet(): Boolean{
        return sharedPreferences.getBoolean(IS_ALARM_SET, false)
    }

    fun setAlarmSet(isSet: Boolean){
        editor.putBoolean(IS_ALARM_SET, isSet).apply()
    }

    fun isNotificationEnabled(): Boolean {
        return sharedPreferences.getBoolean(NOTIFICATION_NEW_EXPRESSION, true)
    }

    fun getNotificationSound(): String? {
        return sharedPreferences.getString(NOTIFICATION_NEW_EXPRESSION_RINGTONE, DEFAULT_NOTIFICATION_NEW_EXPRESSION_RINGTONE)
    }
}