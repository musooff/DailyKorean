package com.dailykorean.app.home.discover.conversation.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * Created by musooff on 01/01/2019.
 */

@Entity
class Conversation {
    @PrimaryKey
    @NonNull
    var public_date: Date? = null
    var orgnc_sentence : String? = null
    var trsl_orgnc_sentence: String? = null
    var speaker: String? = null
    var gender: String? = null
    var disp_seq: Int = 0
}