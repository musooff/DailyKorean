package com.dailykorean.app.home.my.favoriteexpression

import android.content.Context
import androidx.lifecycle.LiveData
import com.dailykorean.app.home.discover.model.FavoriteExpression
import com.dailykorean.app.room.AppDatabase
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by musooff on 02/01/2019.
 */

class FavoriteExpressionRepository(val context: Context){
    fun getFavoriteExpressions(): Single<List<FavoriteExpression>>{
        return getAppDatabase().expressionDao().getFavoriteExpressions(true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
    private fun getAppDatabase() = AppDatabase.getInstance(context)

}