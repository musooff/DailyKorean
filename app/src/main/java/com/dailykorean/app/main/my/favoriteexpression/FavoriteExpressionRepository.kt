package com.dailykorean.app.main.my.favoriteexpression

import android.content.Context
import androidx.lifecycle.LiveData
import com.dailykorean.app.main.discover.model.FavoriteExpression
import com.dailykorean.app.room.AppDatabase
import com.dailykorean.app.utils.Ln
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers

/**
 * Created by musooff on 02/01/2019.
 */

class FavoriteExpressionRepository(val context: Context){
    fun getFavoriteExpressions(): LiveData<List<FavoriteExpression>>{
        return getAppDatabase().expressionDao().getFavoriteExpressions(true)
    }

    fun deleteFavoriteExpressions(items: List<String>){
        Completable.fromAction {
            getAppDatabase().expressionDao().setFavorite(items, false)
        }
                .subscribeOn(Schedulers.io())
                .subscribe({},{Ln.e(it)})

    }
    private fun getAppDatabase() = AppDatabase.getInstance(context)

}