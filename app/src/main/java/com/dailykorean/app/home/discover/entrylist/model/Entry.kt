package com.dailykorean.app.home.discover.entrylist.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.dailykorean.app.home.discover.entrylist.EntryListActivity

/**
 * Created by musooff on 01/01/2019.
 */

@Entity
class Entry {
    @PrimaryKey
    @NonNull
    var id: String? = null
    var conv_id: String? = null
    var orgnc_entry_name: String? = null
        set(value) {
            field = removeOtherMeanings(removeHTML(value!!))
        }
    var mean: String? = null
        set(value) {
            field = removeOtherMeanings(value!!)
        }
    var isFavorite: Boolean = false
    @Ignore
    var shownKind = EntryListActivity.KIND_KOR

    private fun removeHTML(string: String): String {
        var result = string.replace("<b>", "")
        result = result.replace("</b>", "")
        return result
    }

    private fun removeOtherMeanings(translation: String): String {
        var result = translation
        if (result.contains(",")) {
            result = result.substring(0, result.indexOf(","))
        }
        if (result.contains("(")) {
            result = result.substring(0, result.indexOf("("))
        }
        return result
    }
}