package com.dailykorean.app.main.discover.conversation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.dailykorean.app.R
import com.dailykorean.app.common.base.BaseActivity
import com.dailykorean.app.utils.Ln
import kotlinx.android.synthetic.main.activity_conversation.*
import kotlinx.android.synthetic.main.toolbar.*

/**
 * Created by musooff on 01/01/2019.
 */

class ConversationActivity : BaseActivity() {

    companion object {

        const val KIND_ENG = 0
        const val KIND_KOR = 1
        private const val CONV_ID = "expId"

        fun newIntent(context: Context, convId: String) {
            val intent = Intent(context, ConversationActivity::class.java)
            intent.putExtra(CONV_ID, convId)
            context.startActivity(intent)
        }
    }

    private lateinit var repository: ConversationRepository
    private val adapter = ConversationAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)
        initToolbar()

        conversation_rv.adapter = adapter
        conversation_rv.layoutManager = LinearLayoutManager(this)

        val convId = intent.extras?.getString(CONV_ID)!!
        repository = ConversationRepository(applicationContext)

        repository.getConversation(convId)
                .subscribe({
                    adapter.submitList(it)
                }, { Ln.e(it) })

    }

    override fun initToolbar() {
        toolbar.title = getString(R.string.title_conversation)
        super.initToolbar()
    }

}