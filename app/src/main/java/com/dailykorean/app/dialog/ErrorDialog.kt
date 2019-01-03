package com.dailykorean.app.dialog

import android.app.Activity
import android.app.AlertDialog
import com.dailykorean.app.R

/**
 * Created by musooff on 03/01/2019.
 */

object ErrorDialog{

    fun noInternet(activity: Activity, retry: () -> Unit) {
        val builder = AlertDialog.Builder(activity)
        builder.setMessage(R.string.error_tip)
                .setTitle(R.string.error_message)
                .setPositiveButton(R.string.retry) { _, _ ->
                    retry.invoke()
                }
        builder.create()
        builder.show()
    }
}
