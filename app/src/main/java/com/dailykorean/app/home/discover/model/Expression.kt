package com.dailykorean.app.home.discover.model

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.dailykorean.app.home.discover.conversation.model.Sentence
import com.dailykorean.app.home.discover.entrylist.model.Entry
import java.io.Serializable
import java.util.*

/**
 * Created by musooff on 01/01/2019.
 */

@Entity
open class Expression() : Parcelable{
    @PrimaryKey
    @NonNull
    var id:String? = null
    var title: String? = null
    var title_translation: String? = null
    var public_date: Date? = null
    @Ignore
    var sentences: List<Sentence> = arrayListOf()
    @Ignore
    var entrys: List<Entry> = arrayListOf()
    var isFavorite: Boolean = false

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        title = parcel.readString()
        title_translation = parcel.readString()
        isFavorite = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(title_translation)
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

class FavoriteExpression : Expression (){
    var gender: String? = null
    var sentenceId: String? = null
}

class ExpressionResult{
    var data: Expression? = null
}