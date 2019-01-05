package com.dailykorean.app.main.discover.model

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.dailykorean.app.main.discover.conversation.model.Sentence
import com.dailykorean.app.main.discover.entrylist.model.Entry
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

/**
 * Created by musooff on 01/01/2019.
 */

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
open class Expression() : Parcelable {

    @PrimaryKey
    @NonNull
    var id: String? = null

    @JsonProperty(value = "title")
    var titleEnglish: String? = null

    @JsonProperty(value = "title_translation")
    var titleKorean: String? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
    @JsonProperty(value = "public_date")
    var publicDate: Date? = null

    @Ignore
    open var sentences: List<Sentence> = arrayListOf()

    @Ignore
    open var entrys: List<Entry> = arrayListOf()

    var isFavorite: Boolean = false

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        titleEnglish = parcel.readString()
        titleKorean = parcel.readString()
        isFavorite = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(titleEnglish)
        parcel.writeString(titleKorean)
        parcel.writeByte(if (isFavorite) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    @SuppressLint("ParcelCreator")
    companion object CREATOR : Parcelable.Creator<Expression> {
        override fun createFromParcel(parcel: Parcel): Expression {
            return Expression(parcel)
        }

        override fun newArray(size: Int): Array<Expression?> {
            return arrayOfNulls(size)
        }
    }
}

class FavoriteExpression : Expression() {
    var gender: String? = null
}

@JsonIgnoreProperties(ignoreUnknown = true)
class ExpressionResult {
    var data: Expression? = null
}

class HomeExpression : Expression() {
    @Relation(entity = Sentence::class, parentColumn = "id", entityColumn = "expId")
    override var sentences: List<Sentence> = arrayListOf()

    @Relation(entity = Entry::class, parentColumn = "id", entityColumn = "expId")
    override var entrys: List<Entry> = arrayListOf()
}