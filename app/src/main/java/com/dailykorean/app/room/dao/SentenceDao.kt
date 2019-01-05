package com.dailykorean.app.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.dailykorean.app.main.discover.conversation.model.Sentence
import io.reactivex.Single

/**
 * Created by musooff on 02/01/2019.
 */
@Dao
interface SentenceDao: BaseDao<Sentence> {
    @Query("""SELECT * FROM Sentence
        WHERE expId = :expId
        ORDER BY displaySeq ASC""")
    fun getConversation(expId: String): Single<List<Sentence>>

    @Query("SELECT gender FROM Sentence WHERE sentenceKorean = :trsl")
    fun getGender(trsl : String): Single<String>
}