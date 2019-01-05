package com.dailykorean.app.main.discover.conversation.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.dailykorean.app.main.discover.conversation.ConversationActivity
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by musooff on 01/01/2019.
 */

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
class Sentence {

    @PrimaryKey
    @NonNull
    var id: String? = null

    @JsonProperty(value = "conv_id")
    var expId: String? = null

    @JsonProperty(value = "orgnc_sentence")
    var sentenceEnglish : String? = null
        set(value) {
            field = removeHTML(value!!)
        }
    @JsonProperty(value = "trsl_orgnc_sentence")
    var sentenceKorean: String? = null

    var speaker: String? = null

    var gender: String? = null

    @JsonProperty(value = "disp_seq")
    var displaySeq: Int = 0

    @Ignore
    var shownKind = ConversationActivity.KIND_KOR

    private fun removeHTML(string: String): String {
        var result = string.replace("<b>", "")
        result = result.replace("</b>", "")
        return result
    }

}