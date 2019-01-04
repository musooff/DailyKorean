package com.dailykorean.app.utils

/**
 * Created by musooff on 04/01/2019.
 */

object ExpressionUtils {

    /**
     * INNER JOIN shadowing Expression.id with Sentence.id
     * I am stripping Sentence.id to get Expression.id
     * */

    fun getExpressionId(id: String): String {
        if (id.indexOf("_") != -1){
            return id.substring(0, id.indexOf("_"))
        }
        return id
    }
}