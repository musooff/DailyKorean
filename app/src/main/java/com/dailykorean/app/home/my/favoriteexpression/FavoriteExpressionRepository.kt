package com.dailykorean.app.home.my.favoriteexpression

import android.content.Context
import androidx.lifecycle.LiveData
import com.dailykorean.app.home.discover.model.FavoriteExpression
import com.dailykorean.app.room.AppDatabase

/**
 * Created by musooff on 02/01/2019.
 */

class FavoriteExpressionRepository(val context: Context){
    fun getFavoriteExpressions(): LiveData<List<FavoriteExpression>>{
        return getAppDatabase().expressionDao().getFavoriteExpressions(true)
    }
    private fun getAppDatabase() = AppDatabase.getInstance(context)

}