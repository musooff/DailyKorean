package com.dailykorean.app.main.discover.entrylist.model

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.dailykorean.app.main.discover.entrylist.EntryListActivity
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by musooff on 01/01/2019.
 */

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
class Entry() : Parcelable {

    @PrimaryKey
    @NonNull
    var id: String? = null

    @JsonProperty(value = "conv_id")
    var expId: String? = null

    @JsonProperty(value = "orgnc_entry_name")
    var entryEnglish: String? = null
        set(value) {
            field = removeOtherMeanings(removeHTML(value!!))
        }
    @JsonProperty(value = "mean")
    var entryKorean: String? = null
        set(value) {
            field = removeOtherMeanings(value!!)
        }

    var isFavorite: Boolean = false

    @Ignore
    var shownKind = EntryListActivity.KIND_KOR

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        expId = parcel.readString()
        isFavorite = parcel.readByte() != 0.toByte()
        shownKind = parcel.readInt()
    }

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
            result = result.substring(result.indexOf(")") + 2)
        }
        return result
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(expId)
        parcel.writeByte(if (isFavorite) 1 else 0)
        parcel.writeInt(shownKind)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Entry> {
        override fun createFromParcel(parcel: Parcel): Entry {
            return Entry(parcel)
        }

        override fun newArray(size: Int): Array<Entry?> {
            return arrayOfNulls(size)
        }
    }
}