package com.dailykorean.app.main.my

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.dailykorean.app.R
import com.dailykorean.app.main.my.favoriteentry.FavoriteEntryFragment
import com.dailykorean.app.main.my.favoriteexpression.FavoriteExpressionFragment
import com.dailykorean.app.utils.DisplayUtils
import kotlinx.android.synthetic.main.fragment_my.view.*
import kotlinx.android.synthetic.main.my_toolbar.*
import kotlinx.android.synthetic.main.my_toolbar.view.*

/**
 * Created by musooff on 01/01/2019.
 */

class MyFragment: Fragment() {

    private lateinit var currentFragment: EditActionModeFragment

    companion object {
        private const val FAVORITE_EXPRESSION_NAME = "Expression"
        private const val FAVORITE_ENTRY_NAME = "Words and Phrases"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //DisplayUtils.disableFullScreen(activity!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return  inflater.inflate(R.layout.fragment_my, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.toolbar.title = getString(R.string.title_my_tb)
        (activity as AppCompatActivity).setSupportActionBar(view.toolbar)

        view.my_vp.adapter = MyAdapter(activity!!.supportFragmentManager)
        view.my_tl.setupWithViewPager(view.my_vp)

        activity!!.toolbar_delete.setOnClickListener {
            when (currentFragment){
                is FavoriteExpressionFragment -> (currentFragment as FavoriteExpressionFragment).repository.deleteFavoriteExpressions(currentFragment.selectedItems)
                is FavoriteEntryFragment -> (currentFragment as FavoriteEntryFragment).repository.deleteFavoriteEntries(currentFragment.selectedItems)
            }
            currentFragment.updateOptionMenu(false)
        }

    }

    inner class MyAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager){
        override fun getItem(position: Int): Fragment {
            return when (position){
                0 -> FavoriteExpressionFragment()
                1 -> FavoriteEntryFragment()
                else -> Fragment()
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

        override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
            super.setPrimaryItem(container, position, `object`)
            if (::currentFragment.isInitialized){
                if (currentFragment != `object` && currentFragment.isActionMode){
                    currentFragment.updateOptionMenu(false)
                }
            }
            currentFragment = `object` as EditActionModeFragment
        }
    }
}