package com.dailykorean.app.utils

import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.util.*

/**
 * Created by musooff on 01/01/2019.
 */

object DateUtils {

    private const val MILLISECONDS_PER_DAY = 24*60*60*1000
    private val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.KOREAN)
    private val printDateFormat = SimpleDateFormat("d일 M월 yyyy년", Locale.KOREAN)

    fun toString(date: Date?): String? {
        return if (date == null) null else dateFormat.format(date)
    }

    fun toPrintString(date: Date?): String? {
        return if (date == null) null else printDateFormat.format(date)
    }

    fun toDate(date: String?): Date? {
        return if (date == null) null else dateFormat.parse(date)
    }

    fun getToday(): String {
        val cal = Calendar.getInstance()
        return dateFormat.format(cal.time)
    }

    fun getPrevDay(date: Date?): String {
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.DATE, -1)
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
            cal.add(Calendar.DATE, -1)
        }
        return dateFormat.format(cal.time)
    }

    fun daysPassed(date: Date): String{
        val cal = Calendar.getInstance()
        val diff = ((cal.timeInMillis - date.time) / MILLISECONDS_PER_DAY).toInt()

        return when(diff){
            0 -> ""
            1 -> "1 day ago"
            else -> "$diff days ago"
        }
    }

}