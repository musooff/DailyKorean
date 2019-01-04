package com.dailykorean.app.main.my.favoriteentry

import android.content.Context
import androidx.lifecycle.LiveData
import com.dailykorean.app.main.discover.entrylist.model.Entry
import com.dailykorean.app.room.AppDatabase
import com.dailykorean.app.utils.Ln
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers

/**
 * Created by musooff on 02/01/2019.
 */

class FavoriteEntryRepository(val context: Context){
    fun getFavoriteEntries(): LiveData<List<Entry>>{
        return getAppDatabase().entryDao().getFavoriteEntries(true)
    }

    fun deleteFavoriteEntries(items: List<String>){
        Completable.fromAction {
            getAppDatabase().entryDao().setFavorite(items, false)
        }
                .subscribeOn(Schedulers.io())
                .subscribe({},{ Ln.e(it)})
    }

    private fun getAppDatabase() = AppDatabase.getInstance(context)

}