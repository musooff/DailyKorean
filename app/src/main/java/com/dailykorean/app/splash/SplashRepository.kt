package com.dailykorean.app.splash

import android.content.Context
import com.dailykorean.app.main.discover.model.Expression
import com.dailykorean.app.network.ExpressionService
import com.dailykorean.app.room.AppDatabase
import com.dailykorean.app.utils.DateUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by musooff on 03/01/2019.
 */

class SplashRepository(val context: Context) {

    fun updateDatabase(): Observable<Boolean> {
        return Observable.fromCallable {
            getAppDatabase().expressionDao().getLatestExpressionChecking() ?: Expression()
        }
                .flatMap {
                    when {
                        it.publicDate == null -> downloadData(false)
                        it.publicDate != DateUtils.getTodayDate() -> downloadData(true)
                        else -> Observable.just(true)
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    private fun downloadData(canContinue: Boolean): Observable<Boolean>{
        return getExpressionService().getExpression(DateUtils.getTodayString())
                .map { it.data!! }
                .doOnNext { getAppDatabase().expressionDao().insertReplace(it) }
                .doOnNext { getAppDatabase().sentenceDao().insertReplace(it.sentences) }
                .doOnNext { getAppDatabase().entryDao().insertReplace(it.entrys) }
                .map { true }
                .onErrorReturn { canContinue }
    }

    private fun getAppDatabase() = AppDatabase.getInstance(context)

    private fun getExpressionService() = ExpressionService()

}