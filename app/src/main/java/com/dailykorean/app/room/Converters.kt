package com.dailykorean.app.room

import androidx.room.TypeConverter
import com.dailykorean.app.utils.DateUtils
import java.util.*


/**
 * Created by musooff on 01/01/2019.
 */

class Converters{
    @TypeConverter
    fun toDate(value: String?): Date? {
        return DateUtils.toDate(value)
    }

    @TypeConverter
    fun toString(value: Date?): String? {
        return DateUtils.toString(value)
    }
}