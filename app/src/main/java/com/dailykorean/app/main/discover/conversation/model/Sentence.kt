package com.dailykorean.app.main.discover.conversation.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.dailykorean.app.main.discover.conversation.ConversationActivity
import java.util.*

/**
 * Created by musooff on 01/01/2019.
 */

@Entity
class Sentence {
    @PrimaryKey
    @NonNull
    var id: String? = null
    var conv_id: String? = null
    var public_date: Date? = null
    var orgnc_sentence : String? = null
        set(value) {
            field = removeHTML(value!!)
        }
    var trsl_orgnc_sentence: String? = null
    var speaker: String? = null
    var gender: String? = null
    var disp_seq: Int = 0
    @Ignore
    var shownKind = ConversationActivity.KIND_KOR

    private fun removeHTML(string: String): String {
        var result = string.replace("<b>", "")
        result = result.replace("</b>", "")
        return result
    }
}