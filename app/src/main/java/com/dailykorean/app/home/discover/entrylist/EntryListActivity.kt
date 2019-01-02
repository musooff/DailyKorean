package com.dailykorean.app.home.discover.entrylist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dailykorean.app.R
import com.dailykorean.app.common.base.BaseActivity
import com.dailykorean.app.home.discover.entrylist.model.Entry
import com.dailykorean.app.utils.ShareUtils
import kotlinx.android.synthetic.main.activity_entrylist.*
import kotlinx.android.synthetic.main.entrylist_item.view.*
import kotlinx.android.synthetic.main.toolbar.*

/**
 * Created by musooff on 01/01/2019.
 */

class EntryListActivity: BaseActivity() {

    companion object {

        const val KIND_ENG = 0
        const val KIND_KOR = 1
        private const val CONV_ID = "convId"

        fun newIntent(context: Context, convId: String){
            val intent = Intent(context, EntryListActivity::class.java)
            intent.putExtra(CONV_ID, convId)
            context.startActivity(intent)
        }
    }

    private lateinit var repository: EntryListRepository
    private val adapter = EntryListAdapter()
    private var entries:List<Entry> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entrylist)
        initToolbar()

        entrylist_rv.adapter = adapter
        entrylist_rv.layoutManager = LinearLayoutManager(this)
        val convId = intent.extras?.getString(CONV_ID)!!
        repository = EntryListRepository(applicationContext)

        repository.getEntries(convId).observe(this, Observer {
            entries = it
            adapter.notifyDataSetChanged()
        })

    }


    override fun initToolbar() {
        toolbar.title = getString(R.string.title_entrylist)
        super.initToolbar()
    }

    inner class EntryListAdapter : RecyclerView.Adapter<EntryListAdapter.EntryListViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryListViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.entrylist_item, parent, false)
            return EntryListViewHolder(view)
        }

        override fun getItemCount(): Int {
            return entries.size
        }

        override fun onBindViewHolder(holder: EntryListViewHolder, position: Int) {
            holder.bind(entries[position])
        }


        inner class EntryListViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
            fun bind(entry: Entry) {
                view.entrylist_item_tv.text = entry.mean
                if (entry.isFavorite){
                    view.entrylist_item_bookmark.setImageResource(R.drawable.ic_bookmark_black_24dp)
                }
                else{
                    view.entrylist_item_bookmark.setImageResource(R.drawable.ic_bookmark_border_black_24dp)
                }
                view.entrylist_item.setOnClickListener {
                    if (entry.shownKind == KIND_KOR) {
                        view.entrylist_item_tv.text = entry.orgnc_entry_name
                        entry.shownKind = KIND_ENG
                    } else {
                        view.entrylist_item_tv.text = entry.mean
                        entry.shownKind = KIND_KOR
                    }
                }

                view.entrylist_item_share.setOnClickListener {
                    ShareUtils(view.context).shareExpression(it)
                }

                view.entrylist_item_bookmark.setOnClickListener {
                    repository.setFavorite(entry.id!!, !entry.isFavorite)
                }

            }
        }
    }
}