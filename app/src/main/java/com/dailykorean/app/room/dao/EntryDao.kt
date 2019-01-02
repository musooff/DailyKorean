package com.dailykorean.app.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.dailykorean.app.home.discover.entrylist.model.Entry

/**
 * Created by musooff on 02/01/2019.
 */
@Dao
interface EntryDao: BaseDao<Entry> {
    @Query("""SELECT * FROM ENTRY WHERE conv_id = :convId""")
    fun getEntries(convId: String): LiveData<List<Entry>>

    @Query("""SELECT * FROM ENTRY WHERE isFavorite = :isFavorite""")
    fun getFavoriteEntries(isFavorite: Boolean): LiveData<List<Entry>>

    @Query("UPDATE Entry SET isFavorite = :value WHERE id = :id")
    fun setFavorite(id: String, value: Boolean)
}