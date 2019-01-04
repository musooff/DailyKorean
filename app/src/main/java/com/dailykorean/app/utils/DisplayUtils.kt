package com.dailykorean.app.utils

import android.app.Activity
import android.graphics.Color
import android.view.WindowManager
import android.os.Build
import android.view.View


/**
 * Created by musooff on 04/01/2019.
 */

object DisplayUtils {

fun disableFullScreen(activity: Activity){
    if (Build.VERSION.SDK_INT >= 19) {
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE
    }

    if (Build.VERSION.SDK_INT >= 23){
        activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
}

fun enableFullScreen(activity: Activity){
    if ((19 until 21).contains(Build.VERSION.SDK_INT)) {
        setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
    }
    if (Build.VERSION.SDK_INT >= 19) {
        activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }
    if (Build.VERSION.SDK_INT >= 21) {
        setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        activity.window.statusBarColor = Color.TRANSPARENT
    }
}

private fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
    val win = activity.window
    val winParams = win.attributes
    if (on) {
        winParams.flags = winParams.flags or bits
    } else {
        winParams.flags = winParams.flags and bits.inv()
    }
    win.attributes = winParams
}
}