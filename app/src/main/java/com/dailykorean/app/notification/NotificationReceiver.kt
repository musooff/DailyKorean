package com.dailykorean.app.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


/**
 * Created by musooff on 04/01/2019.
 */

class NotificationReceiver : BroadcastReceiver() {

    private lateinit var repository: NotificationRepository

    override fun onReceive(context: Context, intent: Intent) {

        repository = NotificationRepository(context)

        repository.downloadData()
                .subscribe({
                    NotificationService(context).newExpression(it)
                },{
                    NotificationService(context).reminder()
                })
    }
}