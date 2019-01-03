package com.dailykorean.app.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dailykorean.app.main.discover.conversation.model.Sentence
import com.dailykorean.app.main.discover.model.Expression
import com.dailykorean.app.main.discover.entrylist.model.Entry
import com.dailykorean.app.room.dao.EntryDao
import com.dailykorean.app.room.dao.SentenceDao
import com.dailykorean.app.room.dao.ExpressionDao

/**
 * Created by musooff on 01/01/2019.
 */

@Database(
        entities = [
            Expression::class,
            Sentence::class,
            Entry::class
        ],
        version = 1,
        exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun expressionDao(): ExpressionDao
    abstract fun sentenceDao(): SentenceDao
    abstract fun entryDao(): EntryDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context) =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: Room.databaseBuilder(context, AppDatabase::class.java, "appdatabase.db")
                            .fallbackToDestructiveMigration()
                            .build()
                            .also {
                                INSTANCE = it
                            }
                }

    }
}