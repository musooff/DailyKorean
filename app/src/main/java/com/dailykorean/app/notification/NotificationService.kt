package com.dailykorean.app.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.dailykorean.app.R
import com.dailykorean.app.utils.ImageUtils.getImage
import android.graphics.BitmapFactory
import com.dailykorean.app.main.discover.model.Expression
import android.content.Intent
import android.net.Uri
import com.dailykorean.app.AppPreference
import com.dailykorean.app.splash.SplashActivity


/**
 * Created by musooff on 04/01/2019.
 */

class NotificationService(val context: Context) {

    init {
        createNotificationChannel()

    }

    private val appPreference = AppPreference.getInstance(context)

    private val alarmIntent = Intent(context, SplashActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    private val pendingIntent = PendingIntent.getActivity(context, 0, alarmIntent, 0)


    companion object {
        private const val CHANNEL_ID = "expressionNotification"
        private const val DEFAULT_GENDER = "Female"
    }



    fun newExpression(expression: Expression) {

        val mBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.daily_logo)
                .setContentTitle(context.getString(R.string.todays_expression))
                .setContentText("${expression.titleKorean}\n${expression.titleEnglish}")
                .setStyle(NotificationCompat.BigTextStyle()
                        .bigText("${expression.titleKorean}\n${expression.titleEnglish}"))
                .setLargeIcon(
                        BitmapFactory.decodeResource(context.resources, getImage(getGender(expression)))
                )
                .setSound(Uri.parse(appPreference.getNotificationSound()))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(1, mBuilder.build())
        }
    }

    fun reminder() {
        val mBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.daily_logo)
                .setContentTitle(context.getString(R.string.todays_expression_reminder))
                .setContentText(context.getString(R.string.reminder_description))
                .setStyle(NotificationCompat.BigTextStyle()
                        .bigText(context.getString(R.string.reminder_description)))
                .setLargeIcon(
                        BitmapFactory.decodeResource(context.resources, getImage(DEFAULT_GENDER))
                )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(2, mBuilder.build())
        }
    }

    private fun getGender(expression: Expression) : String{
        expression.sentences.forEach { sentence ->
            if (sentence.sentenceEnglish!!.contains(expression.titleEnglish!!)){
                return sentence.gender!!
            }
        }
        return DEFAULT_GENDER
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.channel_name)
            val descriptionText = context.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}