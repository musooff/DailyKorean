package com.dailykorean.app.main.my.favoriteentry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dailykorean.app.R
import com.dailykorean.app.main.discover.entrylist.model.Entry
import com.dailykorean.app.main.my.EditActionModeFragment
import kotlinx.android.synthetic.main.favorite_entry_item.view.*
import kotlinx.android.synthetic.main.fragment_favorite_entry.view.*

/**
 * Created by musooff on 02/01/2019.
 */

class FavoriteEntryFragment : EditActionModeFragment(){
    override fun getEmptyString(): String {
        return getString(R.string.no_fav_entry)
    }

    override fun getActionAdapter(): ActionAdapter {
        return adapter
    }

    lateinit var repository: FavoriteEntryRepository
    private val adapter = FavoriteEntryAdapter()
    private var favoriteEntries: List<Entry> = ArrayList()


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
            favoriteEntries = it
            adapter.notifyDataSetChanged()
            invalidateOptionMenu()
            invalidateEmptyList(view)
        })

    }

    inner class FavoriteEntryAdapter : RecyclerView.Adapter<FavoriteEntryAdapter.FavoriteEntryViewHolder>(), EditActionModeFragment.ActionAdapter {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteEntryViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.favorite_entry_item, parent, false)
            return FavoriteEntryViewHolder(view)
        }

        override fun getItemCount(): Int {
            return favoriteEntries.size
        }

        override fun onBindViewHolder(holder: FavoriteEntryViewHolder, position: Int) {
            holder.bind(favoriteEntries[position])
        }

        inner class FavoriteEntryViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
            fun bind(entry: Entry) {
                view.fav_entry_eng.text = entry.entryEnglish
                view.fav_entry_kor.text = entry.entryKorean

                if (isActionMode) {
                    view.favorite_item_selected.isEnabled = false
                    view.favorite_item_selected.visibility = View.VISIBLE
                }
                else{
                    view.favorite_item_selected.visibility = View.GONE
                }

                view.setOnClickListener {
                    if (isActionMode){
                        updateCheckedItem(entry.id!!, view.favorite_item_selected)
                    }
                }
            }
        }

        override fun isEditable(): Boolean {
            return itemCount > 0
        }
    }
}