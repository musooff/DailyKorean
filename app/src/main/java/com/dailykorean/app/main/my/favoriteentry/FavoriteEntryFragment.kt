package com.dailykorean.app.main.my.favoriteentry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dailykorean.app.R
import kotlinx.android.synthetic.main.fragment_favorite_entry.view.*

/**
 * Created by musooff on 02/01/2019.
 */

class FavoriteEntryFragment : Fragment(){

    private lateinit var repository: FavoriteEntryRepository
    private val adapter = FavoriteEntryAdapter()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favorite_entry, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = FavoriteEntryRepository(context!!)

        val layoutManager = LinearLayoutManager(activity)
        view.favorite_entry_rv.layoutManager = layoutManager
        view.favorite_entry_rv.adapter = adapter

        val dividerItemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        dividerItemDecoration.setDrawable(resources.getDrawable(R.drawable.recycler_view_divider))
        view.favorite_entry_rv.addItemDecoration(dividerItemDecoration)

        repository.getFavoriteEntries().observe(this, Observer {
            adapter.submitList(it)
        })
    }
}