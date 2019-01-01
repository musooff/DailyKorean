package com.dailykorean.app.home.discover.conversation

import android.content.Context
import com.dailykorean.app.home.discover.conversation.model.Sentence
import com.dailykorean.app.room.AppDatabase
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by musooff on 01/01/2019.
 */

class ConversationRepository(val context: Context) {

    fun getConversation(convId: String): Single<List<Sentence>>{
        return getAppDatabase().sentenceDao().getConversation(convId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    private fun getAppDatabase() = AppDatabase.getInstance(context)

}