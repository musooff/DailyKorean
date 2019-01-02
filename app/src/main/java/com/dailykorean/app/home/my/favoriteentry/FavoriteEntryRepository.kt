package com.dailykorean.app.home.my.favoriteentry

import android.content.Context
import androidx.lifecycle.LiveData
import com.dailykorean.app.home.discover.entrylist.model.Entry
import com.dailykorean.app.room.AppDatabase

/**
 * Created by musooff on 02/01/2019.
 */

class FavoriteEntryRepository(val context: Context){
    fun getFavoriteEntries(): LiveData<List<Entry>>{
        return getAppDatabase().entryDao().getFavoriteEntries(true)
    }
    private fun getAppDatabase() = AppDatabase.getInstance(context)

}