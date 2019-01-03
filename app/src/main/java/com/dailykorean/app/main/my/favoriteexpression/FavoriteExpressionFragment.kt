package com.dailykorean.app.main.my.favoriteexpression

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dailykorean.app.R
import com.dailykorean.app.dialog.FavoriteExpressionDialog
import com.dailykorean.app.main.discover.model.FavoriteExpression
import com.dailykorean.app.utils.ImageUtils.getImage
import kotlinx.android.synthetic.main.favorite_expression_item.view.*
import kotlinx.android.synthetic.main.fragment_favorite_expression.view.*
import androidx.recyclerview.widget.DividerItemDecoration



/**
 * Created by musooff on 02/01/2019.
 */

class FavoriteExpressionFragment : Fragment(){

    private lateinit var repository: FavoriteExpressionRepository
    private val adapter = FavoriteExpressionAdapter()
    private var favoriteExpressions: List<FavoriteExpression> = ArrayList()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_favorite_expression, container, false)

        repository = FavoriteExpressionRepository(context!!)

        val layoutManager = LinearLayoutManager(activity)
        view.favorite_exp_rv.layoutManager = layoutManager
        view.favorite_exp_rv.adapter = adapter

        val dividerItemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        dividerItemDecoration.setDrawable(resources.getDrawable(R.drawable.recycler_view_divider))
        view.favorite_exp_rv.addItemDecoration(dividerItemDecoration)


        repository.getFavoriteExpressions()
                .subscribe({
                    favoriteExpressions = it
                    adapter.notifyDataSetChanged()
                }, {})
        return view
    }

    inner class FavoriteExpressionAdapter : RecyclerView.Adapter<FavoriteExpressionAdapter.FavoriteExpressionViewHolder>() {
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
                view.fav_exp__thumb_iv.setImageResource(getImage(expression.gender!!))

                view.setOnClickListener {
                    val dialog = FavoriteExpressionDialog.newIntent(expression)
                    fragmentManager!!.beginTransaction().add(dialog, tag).commitAllowingStateLoss()
                }
            }
        }
    }
}