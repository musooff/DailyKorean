package com.dailykorean.app.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dailykorean.app.R
import com.dailykorean.app.main.discover.conversation.ConversationAdapter
import com.dailykorean.app.main.my.favoriteentry.FavoriteEntryAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*

/**
 * Created by musooff on 01/01/2019.
 */

class HomeFragment: Fragment() {

    private lateinit var repository: HomeRepository
    private val conversationAdapter = ConversationAdapter()
    private val entryAdapter = FavoriteEntryAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = HomeRepository(context!!)

        home_conversation.adapter = conversationAdapter
        home_conversation.layoutManager = LinearLayoutManager(context)

        home_entries.adapter = entryAdapter
        val layoutManager = LinearLayoutManager(context)
        home_entries.layoutManager = layoutManager

        val dividerItemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        dividerItemDecoration.setDrawable(resources.getDrawable(R.drawable.recycler_view_divider))
        home_entries.addItemDecoration(dividerItemDecoration)


        repository.getLatestExpression()
                .subscribe({
                    view.home_header_title.text = "${it.title_translation}\n\n${it.title}"
                    conversationAdapter.submitList(it.sentences)
                    entryAdapter.submitList(it.entrys)
                },{})
    }
}