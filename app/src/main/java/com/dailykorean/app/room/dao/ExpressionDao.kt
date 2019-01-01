package com.dailykorean.app.room.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import com.dailykorean.app.home.discover.model.Expression
import java.util.*

/**
 * Created by musooff on 01/01/2019.
 */

@Dao
interface ExpressionDao : BaseDao<Expression> {

    @Query("SELECT * FROM Expression")
    fun getExpressions(): DataSource.Factory<Int, Expression>

    @Query("UPDATE Expression SET isFavorite = :value WHERE public_date = :date")
    fun setFavorite(date: Date, value: Boolean)
}