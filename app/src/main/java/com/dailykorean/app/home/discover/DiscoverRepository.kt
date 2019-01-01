package com.dailykorean.app.home.discover

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.dailykorean.app.home.discover.model.Expression
import com.dailykorean.app.network.ExpressionService
import com.dailykorean.app.room.AppDatabase
import com.dailykorean.app.utils.DateUtils
import com.dailykorean.app.utils.Ln
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * Created by musooff on 01/01/2019.
 */

class DiscoverRepository(val context: Context) {

    companion object {
        private const val PAGE_LIMIT = 10
    }

    fun getExpressions(): LiveData<PagedList<Expression>>{
        val boundaryCallback = ExpressionBoundaryCallback()
        return LivePagedListBuilder(getAppDatabase().expressionDao().getExpressions(), PAGE_LIMIT)
                .setBoundaryCallback(boundaryCallback)
                .build()
    }

    fun setFavorite(date: Date, value: Boolean){
        Completable.fromAction {
            getAppDatabase().expressionDao().setFavorite(date, value)
        }
                .subscribeOn(Schedulers.io())
                .subscribe({}, {Ln.e(it)})
    }

    private fun getAppDatabase() = AppDatabase.getInstance(context)

    private fun getExpressionService() = ExpressionService()

    inner class ExpressionBoundaryCallback : PagedList.BoundaryCallback<Expression>() {
        override fun onZeroItemsLoaded() {
            getExpressionService().getExpression(DateUtils.getToday())
                    .subscribe({
                        getAppDatabase().expressionDao().insertReplace(it.data!!)
                    }, { Ln.e(it)})
        }

        override fun onItemAtEndLoaded(itemAtEnd: Expression) {
            getExpressionService().getExpression(DateUtils.getPrevDay(itemAtEnd.public_date))
                    .subscribe({
                        Ln.i("Downloaded ${it.data!!.public_date}")
                        getAppDatabase().expressionDao().insertReplace(it.data!!)
                    }, { Ln.e(it)})
        }
    }


}

