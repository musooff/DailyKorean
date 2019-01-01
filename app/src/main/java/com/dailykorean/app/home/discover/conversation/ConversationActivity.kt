package com.dailykorean.app.home.discover.conversation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dailykorean.app.R
import com.dailykorean.app.home.discover.conversation.model.Sentence
import com.dailykorean.app.utils.Ln
import kotlinx.android.synthetic.main.activity_conversation.*
import kotlinx.android.synthetic.main.conversation_item.view.*

/**
 * Created by musooff on 01/01/2019.
 */

class ConversationActivity : AppCompatActivity() {

    companion object {

        const val KIND_ENG = 0
        const val KIND_KOR = 1
        private const val CONV_ID = "convId"

        fun newIntent(context: Context, convId: String) {
            val intent = Intent(context, ConversationActivity::class.java)
            intent.putExtra(CONV_ID, convId)
            context.startActivity(intent)
        }
    }

    private lateinit var repository: ConversationRepository
    private val adapter = ConversationAdapter()
    private val sentences = ArrayList<Sentence>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)

        conversation_rv.adapter = adapter
        conversation_rv.layoutManager = LinearLayoutManager(this)

        val convId = intent.extras?.getString(CONV_ID)!!
        repository = ConversationRepository(applicationContext)

        repository.getConversation(convId)
                .subscribe({
                    sentences.addAll(it)
                    adapter.notifyDataSetChanged()
                }, { Ln.e(it) })
    }

    inner class ConversationAdapter : RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.conversation_item, parent, false)
            return ConversationViewHolder(view)
        }

        override fun getItemCount(): Int {
            return sentences.size
        }

        override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
            holder.bind(sentences[position])
        }


        inner class ConversationViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
            fun bind(sentence: Sentence) {
                if (sentence.speaker == "A") {
                    view.conversation_item_right.visibility = View.GONE
                    view.conversation_item_left_text_tv.text = sentence.trsl_orgnc_sentence
                    view.conversation_item_left_thumb_iv.setImageResource(getImage(sentence.gender!!))
                    view.setOnClickListener {
                        if (sentence.shownKind == KIND_ENG) {
                            view.conversation_item_left_text_tv.text = sentence.trsl_orgnc_sentence
                            sentence.shownKind = KIND_KOR
                        } else {
                            view.conversation_item_left_text_tv.text = sentence.orgnc_sentence
                            sentence.shownKind = KIND_ENG
                        }
                    }
                } else {
                    view.conversation_item_left.visibility = View.GONE
                    view.conversation_item_right_text_tv.text = sentence.trsl_orgnc_sentence
                    view.conversation_item_right_thumb_iv.setImageResource(getImage(sentence.gender!!))
                    view.setOnClickListener {
                        if (sentence.shownKind == KIND_ENG) {
                            view.conversation_item_right_text_tv.text = sentence.trsl_orgnc_sentence
                            sentence.shownKind = KIND_KOR
                        } else {
                            view.conversation_item_right_text_tv.text = sentence.orgnc_sentence
                            sentence.shownKind = KIND_ENG
                        }
                    }

                }
            }
        }

        private fun getImage(gender: String): Int {
            return when (gender) {
                "Male" -> R.drawable.male_1
                "Female" -> R.drawable.female_1
                "Boy" -> R.drawable.boy_1
                "Girl" -> R.drawable.girl_1
                "Male1" -> R.drawable.male_1
                "Male2" -> R.drawable.male_2
                "Male3" -> R.drawable.male_3
                "Female1" -> R.drawable.female_1
                "Female2" -> R.drawable.female_2
                "Female3" -> R.drawable.female_3
                "Girl1" -> R.drawable.girl_1
                "Girl2" -> R.drawable.girl_2
                "Boy2" -> R.drawable.boy_2
                "Boy1" -> R.drawable.boy_1
                else -> R.drawable.boy_1
            }
        }
    }
}