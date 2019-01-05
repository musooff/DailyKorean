package com.dailykorean.app.dialog

import android.content.Context
import com.dailykorean.app.main.discover.model.Expression
import com.dailykorean.app.network.ExpressionService
import com.dailykorean.app.room.AppDatabase
import com.dailykorean.app.utils.DateUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * Created by musooff on 03/01/2019.
 */

class DialogRepository(val context: Context) {

    fun getExpression(date: Date): Observable<Expression> {
        return Observable.fromCallable {
            getAppDatabase().expressionDao().getExpression(date) ?: Expression()
        }
                .flatMap {
                    if (it.publicDate == null){
                        getExpressionServer(date)
                    }
                    else {
                        Observable.just(it)
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun getExpressionServer(date: Date): Observable<Expression>{
        return getExpressionService().getExpression(DateUtils.toString(date)!!)
                .map { it.data!! }
                .doOnNext { getAppDatabase().expressionDao().insertReplace(it) }
                .doOnNext { getAppDatabase().sentenceDao().insertReplace(it.sentences) }
                .doOnNext { getAppDatabase().entryDao().insertReplace(it.entrys) }
    }

    private fun getExpressionService() = ExpressionService()
    private fun getAppDatabase() = AppDatabase.getInstance(context)
}