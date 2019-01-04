package com.dailykorean.app.main.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dailykorean.app.R
import com.dailykorean.app.main.discover.entrylist.model.Entry
import kotlinx.android.synthetic.main.favorite_entry_item.view.*

class HomeEntryAdapter : RecyclerView.Adapter<HomeEntryAdapter.HomeEntryViewHolder>(){

    private var favoriteEntries: List<Entry> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeEntryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.favorite_entry_item, parent, false)
        return HomeEntryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return favoriteEntries.size
    }

    override fun onBindViewHolder(holder: HomeEntryViewHolder, position: Int) {
        holder.bind(favoriteEntries[position])
    }

    fun submitList(entries: List<Entry>) {
        this.favoriteEntries = entries
        notifyDataSetChanged()
    }

    inner class HomeEntryViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(entry: Entry) {
            view.fav_entry_eng.text = entry.orgnc_entry_name
            view.fav_entry_kor.text = entry.mean
        }
    }
}