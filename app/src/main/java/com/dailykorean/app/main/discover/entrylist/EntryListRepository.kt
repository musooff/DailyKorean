package com.dailykorean.app.main.discover.entrylist

import android.content.Context
import androidx.lifecycle.LiveData
import com.dailykorean.app.main.discover.entrylist.model.Entry
import com.dailykorean.app.room.AppDatabase
import com.dailykorean.app.utils.Ln
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers

/**
 * Created by musooff on 01/01/2019.
 */

class EntryListRepository(val context: Context) {

    fun getEntries(convId: String): LiveData<List<Entry>>{
        return getAppDatabase().entryDao().getEntries(convId)
    }

    fun setFavorite(id: String, value: Boolean){
        Completable.fromAction {
            getAppDatabase().entryDao().setFavorite(id, value)
        }
                .subscribeOn(Schedulers.io())
                .subscribe({}, { Ln.e(it)})
    }

    private fun getAppDatabase() = AppDatabase.getInstance(context)

}