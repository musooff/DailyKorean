package com.dailykorean.app.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.dailykorean.app.R
import com.dailykorean.app.main.discover.conversation.ConversationActivity
import com.dailykorean.app.main.discover.entrylist.EntryListActivity
import com.dailykorean.app.main.discover.model.Expression
import com.dailykorean.app.utils.DateUtils
import com.dailykorean.app.utils.ShareUtils
import kotlinx.android.synthetic.main.expression_item.*

/**
 * Created by musooff on 02/01/2019.
 */

class FavoriteExpressionDialog : DialogFragment(){

    companion object {

        private const val EXPRESSION = "expression"

        fun  newIntent(expression: Expression) = FavoriteExpressionDialog().apply {
            val args = Bundle()
            args.putParcelable(EXPRESSION, expression)
            arguments = args
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(activity).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.expression_item, container, true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        expression_item_header.visibility = View.GONE
        expression_item_footer.visibility = View.GONE
        expression_item_share.layoutParams = expression_item_favorite.layoutParams
        expression_item_favorite.visibility = View.GONE


        arguments?.getParcelable<Expression>(EXPRESSION)?.let { expression ->
            expression_item_title.text = "${expression.title}\n\n${expression.title_translation}"
            expression_item_date.text = DateUtils.toString(expression.public_date)

            expression_item_translate.setOnClickListener {
                EntryListActivity.newIntent(view.context, getExpressionId(expression.id!!))
                this.dismiss()
            }

            expression_item_conversation.setOnClickListener {
                ConversationActivity.newIntent(view.context, getExpressionId(expression.id!!))
                this.dismiss()
            }

            expression_item_share.setOnClickListener {
                ShareUtils(view.context).shareExpression(activity!!, it)
            }
        }

    }

    /**
     * INNER JOIN shadowing Expression.id with Sentence.id
     * I am stripping Sentence.id to get Expression.id
     * */

    private fun getExpressionId(id: String): String {
        if (id.indexOf("_") != -1){
            return id.substring(0, id.indexOf("_"))
        }
        return id
    }
}
