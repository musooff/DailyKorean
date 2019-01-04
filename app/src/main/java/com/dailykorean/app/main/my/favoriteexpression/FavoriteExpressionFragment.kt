package com.dailykorean.app.main.my.favoriteexpression

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dailykorean.app.R
import com.dailykorean.app.dialog.FavoriteExpressionDialog
import com.dailykorean.app.main.discover.model.FavoriteExpression
import com.dailykorean.app.utils.ImageUtils.getImage
import kotlinx.android.synthetic.main.favorite_expression_item.view.*
import kotlinx.android.synthetic.main.fragment_favorite_expression.view.*
import androidx.recyclerview.widget.DividerItemDecoration
import com.dailykorean.app.main.my.EditActionModeFragment
import com.dailykorean.app.utils.ExpressionUtils.getExpressionId


/**
 * Created by musooff on 02/01/2019.
 */

class FavoriteExpressionFragment : EditActionModeFragment(){

    override fun getActionAdapter(): ActionAdapter {
        return adapter
    }

    lateinit var repository: FavoriteExpressionRepository
    private val adapter = FavoriteExpressionAdapter()
    private var favoriteExpressions: List<FavoriteExpression> = ArrayList()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favorite_expression, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = FavoriteExpressionRepository(context!!)

        val layoutManager = LinearLayoutManager(activity)
        view.favorite_exp_rv.layoutManager = layoutManager
        view.favorite_exp_rv.adapter = adapter

        val dividerItemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        dividerItemDecoration.setDrawable(resources.getDrawable(R.drawable.recycler_view_divider))
        view.favorite_exp_rv.addItemDecoration(dividerItemDecoration)


        repository.getFavoriteExpressions().observe(this, Observer {
            favoriteExpressions = it
            adapter.notifyDataSetChanged()
            invalidateOptionMenu()
        })
    }


    inner class FavoriteExpressionAdapter : RecyclerView.Adapter<FavoriteExpressionAdapter.FavoriteExpressionViewHolder>(), ActionAdapter {

        override fun isEditable(): Boolean {
            return itemCount > 0
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteExpressionViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.favorite_expression_item, parent, false)
            return FavoriteExpressionViewHolder(view)
        }

        override fun getItemCount(): Int {
            return favoriteExpressions.size
        }

        override fun onBindViewHolder(holder: FavoriteExpressionViewHolder, position: Int) {
            holder.bind(favoriteExpressions[position])
        }


        inner class FavoriteExpressionViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
            fun bind(expression: FavoriteExpression) {
                view.fav_exp_eng.text = expression.title_translation
                view.fav_exp_kor.text = expression.title
                view.fav_exp_thumb_iv.setImageResource(getImage(expression.gender!!))

                if (isActionMode) {
                    view.favorite_item_selected.isEnabled = false
                    view.favorite_item_selected.visibility = View.VISIBLE
                }
                else{
                    view.favorite_item_selected.visibility = View.GONE
                }

                view.setOnClickListener {
                    if (isActionMode) {
                        updateCheckedItem(getExpressionId(expression.id!!), view.favorite_item_selected)
                    }
                    else {
                        val dialog = FavoriteExpressionDialog.newIntent(expression)
                        fragmentManager!!.beginTransaction().add(dialog, tag).commitAllowingStateLoss()
                    }

                }
            }
        }
    }
}