package com.dailykorean.app.main.discover.conversation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dailykorean.app.R
import com.dailykorean.app.main.discover.conversation.ConversationActivity.Companion.KIND_ENG
import com.dailykorean.app.main.discover.conversation.ConversationActivity.Companion.KIND_KOR
import com.dailykorean.app.main.discover.conversation.model.Sentence
import com.dailykorean.app.utils.ImageUtils.getImage
import kotlinx.android.synthetic.main.conversation_item.view.*

class ConversationAdapter : RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder>() {
    private var sentences: List<Sentence> = ArrayList()

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

    fun submitList(sentences: List<Sentence>){
        this.sentences = sentences
        notifyDataSetChanged()
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


}