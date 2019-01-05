package com.dailykorean.app.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dailykorean.app.R
import com.dailykorean.app.main.discover.conversation.ConversationAdapter
import com.dailykorean.app.utils.DisplayUtils
import com.dailykorean.app.utils.ImageUtils.getImage
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*

/**
 * Created by musooff on 01/01/2019.
 */

class HomeFragment: Fragment() {

    private lateinit var repository: HomeRepository
    private val conversationAdapter = ConversationAdapter()
    private val entryAdapter = HomeEntryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DisplayUtils.enableFullScreen(activity!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        repository = HomeRepository(context!!)

        home_conversation.adapter = conversationAdapter
        home_conversation.layoutManager = LinearLayoutManager(context) as RecyclerView.LayoutManager?

        home_entries.adapter = entryAdapter
        val layoutManager = LinearLayoutManager(context)
        home_entries.layoutManager = layoutManager

        val dividerItemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        dividerItemDecoration.setDrawable(resources.getDrawable(R.drawable.recycler_view_divider))
        home_entries.addItemDecoration(dividerItemDecoration)


        repository.getLatestExpression()
                .subscribe({
                    view.home_header_title.text = "${it.titleKorean}\n\n${it.titleEnglish}"
                    it.sentences.forEach { sentence ->
                        if (sentence.sentenceEnglish!!.contains(it.titleEnglish!!)){
                            view.home_header_thumb.setImageDrawable(resources.getDrawable(getImage(sentence.gender!!)))
                        }
                    }
                    conversationAdapter.submitList(it.sentences)
                    entryAdapter.submitList(it.entrys)
                },{})
    }
}