package com.dailykorean.app.room.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import com.dailykorean.app.home.discover.model.Expression
import com.dailykorean.app.home.discover.model.FavoriteExpression
import io.reactivex.Single

/**
 * Created by musooff on 01/01/2019.
 */

@Dao
interface ExpressionDao : BaseDao<Expression> {

    @Query("SELECT * FROM Expression ORDER BY public_date DESC")
    fun getExpressions(): DataSource.Factory<Int, Expression>

    @Query("SELECT * FROM Expression INNER JOIN Sentence ON Sentence.trsl_orgnc_sentence LIKE '%'||Expression.title_translation||'%' WHERE isFavorite = :isFavorite ORDER BY public_date DESC")
    fun getFavoriteExpressions(isFavorite: Boolean): Single<List<FavoriteExpression>>

    @Query("UPDATE Expression SET isFavorite = :value WHERE id = :id")
    fun setFavorite(id: String, value: Boolean)
}