package com.dailykorean.app.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.dailykorean.app.main.discover.entrylist.model.Entry

/**
 * Created by musooff on 02/01/2019.
 */
@Dao
interface EntryDao: BaseDao<Entry> {
    @Query("""SELECT * FROM ENTRY WHERE expId = :expId""")
    fun getEntries(expId: String): LiveData<List<Entry>>

    @Query("""SELECT * FROM ENTRY WHERE isFavorite = :isFavorite""")
    fun getFavoriteEntries(isFavorite: Boolean): LiveData<List<Entry>>

    @Query("UPDATE Entry SET isFavorite = :value WHERE id = :id")
    fun setFavorite(id: String, value: Boolean)

    @Query("UPDATE Entry SET isFavorite = :value WHERE id IN (:ids)")
    fun setFavorite(ids: List<String>, value: Boolean)
}