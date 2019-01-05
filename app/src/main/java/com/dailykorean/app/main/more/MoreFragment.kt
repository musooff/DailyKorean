package com.dailykorean.app.main.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dailykorean.app.R
import com.dailykorean.app.dialog.DatePickerFragment
import com.dailykorean.app.dialog.DialogRepository
import com.dailykorean.app.dialog.ErrorDialog
import com.dailykorean.app.dialog.FavoriteExpressionDialog
import com.dailykorean.app.main.discover.model.Expression
import com.dailykorean.app.main.more.settings.SettingsActivity
import com.dailykorean.app.utils.DisplayUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_more.view.*
import kotlinx.android.synthetic.main.toolbar.view.*
import java.util.*

/**
 * Created by musooff on 01/01/2019.
 */

class MoreFragment: Fragment() {

    private lateinit var repository: DialogRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DisplayUtils.disableFullScreen(activity!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_more, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.toolbar.title = getString(R.string.title_more)

        repository = DialogRepository(context!!)

        view.more_settings.setOnClickListener {
            SettingsActivity.newIntent(context!!)
        }

        view.more_goto.setOnClickListener {
            val datePickerFragment = DatePickerFragment()

            datePickerFragment.setDatePickerResult(object : DatePickerFragment.DatePickerResult{
                override fun onFinish(expression: Expression) {
                    val dialog = FavoriteExpressionDialog.newIntent(expression)
                    fragmentManager!!.beginTransaction().add(dialog, tag).commitAllowingStateLoss()
                }

                override fun onError(date: Date) {
                    ErrorDialog.noInternet(activity!!) {
                        repository.getExpressionServer(date)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({
                                    val dialog = FavoriteExpressionDialog.newIntent(it)
                                    fragmentManager!!.beginTransaction().add(dialog, tag).commitAllowingStateLoss()
                                }, {
                                    ErrorDialog.noInternet(activity!!) {retry(date)}
                                })
                    }
                }
            })

            datePickerFragment.show(fragmentManager!!, tag)
        }

    }

    private fun retry(date: Date){
        repository.getExpressionServer(date)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val dialog = FavoriteExpressionDialog.newIntent(it)
                    fragmentManager!!.beginTransaction().add(dialog, tag).commitAllowingStateLoss()
                }, {
                    ErrorDialog.noInternet(activity!!) {retry(date)}
                })
    }
}