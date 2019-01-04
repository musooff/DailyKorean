package com.dailykorean.app.notification

import android.content.Context
import com.dailykorean.app.main.discover.model.Expression
import com.dailykorean.app.network.ExpressionService
import com.dailykorean.app.room.AppDatabase
import com.dailykorean.app.utils.DateUtils
import com.dailykorean.app.utils.Ln
import io.reactivex.Observable

/**
 * Created by musooff on 04/01/2019.
 */

class NotificationRepository(val context: Context) {

    fun downloadData(): Observable<Expression> {
        return getExpressionService().getExpression(DateUtils.getTodayString())
                .map { it.data!! }
                .doOnNext { getAppDatabase().expressionDao().insertReplace(it) }
                .doOnNext { getAppDatabase().sentenceDao().insertReplace(it.sentences) }
                .doOnNext { getAppDatabase().entryDao().insertReplace(it.entrys) }
    }

    private fun getExpressionService() = ExpressionService()

    private fun getAppDatabase() = AppDatabase.getInstance(context)
}