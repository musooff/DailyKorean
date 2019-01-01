package com.dailykorean.app.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.dailykorean.app.home.discover.conversation.model.Sentence
import io.reactivex.Single
import java.util.*

/**
 * Created by musooff on 02/01/2019.
 */
@Dao
interface SentenceDao: BaseDao<Sentence> {
    @Query("""SELECT * FROM Sentence
        WHERE conv_id = :convId
        ORDER BY disp_seq ASC""")
    fun getConversation(convId: String): Single<List<Sentence>>
}