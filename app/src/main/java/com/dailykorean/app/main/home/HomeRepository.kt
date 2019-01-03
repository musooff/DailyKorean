package com.dailykorean.app.main.home

import android.content.Context
import com.dailykorean.app.main.discover.model.Expression
import com.dailykorean.app.main.discover.model.HomeExpression
import com.dailykorean.app.room.AppDatabase
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by musooff on 03/01/2019.
 */

class HomeRepository(val context: Context) {

    fun getLatestExpression(): Single<HomeExpression>{
        return getAppDatabase().expressionDao().getLatestExpression()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    private fun getAppDatabase() = AppDatabase.getInstance(context)
}