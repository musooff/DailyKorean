package com.dailykorean.app.home.discover.conversation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dailykorean.app.R

/**
 * Created by musooff on 01/01/2019.
 */

class ConversationActivity : AppCompatActivity() {

    companion object {
        fun newIntent(context: Context){
            val intent = Intent(context, ConversationActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val repository = ConversationRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)
    }
}