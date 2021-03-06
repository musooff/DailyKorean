package com.dailykorean.app.room.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import com.dailykorean.app.main.discover.model.Expression
import com.dailykorean.app.main.discover.model.FavoriteExpression
import com.dailykorean.app.main.discover.model.HomeExpression
import io.reactivex.Single
import java.util.*

/**
 * Created by musooff on 01/01/2019.
 */

@Dao
interface ExpressionDao : BaseDao<Expression> {

    @Query("SELECT * FROM Expression ORDER BY publicDate DESC")
    fun getExpressions(): DataSource.Factory<Int, Expression>

    @Query("SELECT * FROM Expression ORDER BY publicDate DESC LIMIT 1")
    fun getLatestExpression(): Single<HomeExpression>

    @Query("SELECT * FROM Expression ORDER BY publicDate DESC LIMIT 1")
    fun getLatestExpressionChecking(): Expression?

    @Query("SELECT * FROM Expression WHERE publicDate = :date")
    fun getExpression(date: Date): Expression?

    @Query("SELECT * FROM Expression INNER JOIN Sentence ON Sentence.sentenceEnglish LIKE '%'||Expression.titleEnglish||'%' WHERE isFavorite = :isFavorite ORDER BY publicDate DESC")
    fun getFavoriteExpressions(isFavorite: Boolean): LiveData<List<FavoriteExpression>>

    @Query("UPDATE Expression SET isFavorite = :value WHERE id = :id")
    fun setFavorite(id: String, value: Boolean)

    @Query("UPDATE Expression SET isFavorite = :value WHERE id IN (:ids)")
    fun setFavorite(ids: List<String>, value: Boolean)
}