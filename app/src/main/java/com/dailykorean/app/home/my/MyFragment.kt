package com.dailykorean.app.home.my

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.dailykorean.app.R
import com.dailykorean.app.home.my.favoriteentry.FavoriteEntryFragment
import com.dailykorean.app.home.my.favoriteexpression.FavoriteExpressionFragment
import kotlinx.android.synthetic.main.fragment_my.view.*
import kotlinx.android.synthetic.main.toolbar.view.*

/**
 * Created by musooff on 01/01/2019.
 */

class MyFragment: Fragment() {

    companion object {
        private const val FAVORITE_EXPRESSION_NAME = "Expression"
        private const val FAVORITE_ENTRY_NAME = "Words and Phrases"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return  inflater.inflate(R.layout.fragment_my, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.toolbar.title = getString(R.string.title_my_tb)

        view.my_vp.adapter = MyAdapter(activity!!.supportFragmentManager)
        view.my_tl.setupWithViewPager(view.my_vp)

    }

    inner class MyAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager){
        override fun getItem(position: Int): Fragment {
            return when (position){
                0 -> FavoriteExpressionFragment()
                1 -> FavoriteEntryFragment()
                else -> FavoriteExpressionFragment()
            }
        }

        override fun getCount(): Int {
            return 2
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position){
                0 -> FAVORITE_EXPRESSION_NAME
                1 -> FAVORITE_ENTRY_NAME
                else -> ""
            }
        }
    }
}