package com.dailykorean.app.home.discover.model

import androidx.annotation.NonNull
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.dailykorean.app.home.discover.conversation.model.Conversation
import java.util.*

/**
 * Created by musooff on 01/01/2019.
 */

@Entity
class Expression {
    var title: String? = null
    var title_translation: String? = null
    @PrimaryKey
    @NonNull
    var public_date: Date? = null
    @Ignore
    var sentences: List<Conversation> = arrayListOf()
    var isFavorite: Boolean = false
}

class ExpressionResult{
    var data: Expression? = null
}