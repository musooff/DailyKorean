package com.dailykorean.app.home.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dailykorean.app.R
import com.dailykorean.app.home.discover.conversation.ConversationActivity
import com.dailykorean.app.home.discover.model.Expression
import com.dailykorean.app.home.discover.entrylist.EntryListActivity
import com.dailykorean.app.utils.DateUtils
import com.dailykorean.app.utils.ShareUtils
import kotlinx.android.synthetic.main.expression_item.view.*
import kotlinx.android.synthetic.main.fragment_discover.view.*
import kotlinx.android.synthetic.main.toolbar.view.*

/**
 * Created by musooff on 01/01/2019.
 */

class DiscoverFragment: Fragment() {

    private lateinit var repository: DiscoverRepository
    private val expressionAdapter = ExpressionAdapter()

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Expression>() {
            override fun areItemsTheSame(oldConcert: Expression, newConcert: Expression): Boolean =
                    oldConcert.public_date == newConcert.public_date

            override fun areContentsTheSame(oldConcert: Expression, newConcert: Expression): Boolean =
                    oldConcert == newConcert
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_discover, container, false)

        view.toolbar.title = getString(R.string.title_expressions)

        repository = DiscoverRepository(context!!)

        view.discover_rv.layoutManager = LinearLayoutManager(context)
        view.discover_rv.adapter = expressionAdapter

        repository.getExpressions().observe(this, Observer {
            expressionAdapter.submitList(it)
        })
        return view
    }

    inner class ExpressionAdapter : PagedListAdapter<Expression, ExpressionAdapter.ExpressionViewHolder> (DIFF_CALLBACK){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpressionViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.expression_item, parent, false)
            return ExpressionViewHolder(view)
        }

        override fun onBindViewHolder(holder: ExpressionViewHolder, position: Int) {
            val concert = getItem(position)
            if (concert != null) {
                holder.bind(concert)
            } else {
                holder.clear()
            }
        }

        inner class ExpressionViewHolder(val view: View): RecyclerView.ViewHolder(view){
            fun bind(expression: Expression){
                view.expression_item_title.text = "${expression.title}\n\n${expression.title_translation}"
                view.expression_item_date.text = DateUtils.toPrintString(expression.public_date)
                view.expression_item_dates_passed.text = DateUtils.daysPassed(expression.public_date!!)

                if (expression.isFavorite){
                    view.expression_item_favorite.setImageResource(R.drawable.ic_star_black_24dp)
                }
                else{
                    view.expression_item_favorite.setImageResource(R.drawable.ic_star_border_black_24dp)
                }

                view.expression_item_translate.setOnClickListener {
                    EntryListActivity.newIntent(view.context, expression.id!!)
                }

                view.expression_item_conversation.setOnClickListener {
                    ConversationActivity.newIntent(view.context, expression.id!!)
                }

                view.expression_item_share.setOnClickListener {
                    ShareUtils(view.context).shareExpression(activity!!, it)
                }

                view.expression_item_favorite.setOnClickListener {
                    repository.setFavorite(expression.id!!, !expression.isFavorite)
                }
            }

            fun clear(){

            }
        }
    }
}