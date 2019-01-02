package com.dailykorean.app.utils

import com.dailykorean.app.R

/**
 * Created by musooff on 02/01/2019.
 */

object ImageUtils {

    fun getImage(gender: String): Int {
        return when (gender) {
            "Male" -> R.drawable.male_1
            "Female" -> R.drawable.female_1
            "Boy" -> R.drawable.boy_1
            "Girl" -> R.drawable.girl_1
            "Male1" -> R.drawable.male_1
            "Male2" -> R.drawable.male_2
            "Male3" -> R.drawable.male_3
            "Female1" -> R.drawable.female_1
            "Female2" -> R.drawable.female_2
            "Female3" -> R.drawable.female_3
            "Girl1" -> R.drawable.girl_1
            "Girl2" -> R.drawable.girl_2
            "Boy2" -> R.drawable.boy_2
            "Boy1" -> R.drawable.boy_1
            else -> R.drawable.boy_1
        }
    }
}