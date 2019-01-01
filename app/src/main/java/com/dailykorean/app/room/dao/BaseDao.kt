package com.dailykorean.app.room.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

/**
 * Created by musooff on 01/01/2019.
 */

interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReplace(obj: T)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertIgnore(obj: T)

    @Delete
    fun delete(obj: T)

    @Update
    fun update(obj: T)
}