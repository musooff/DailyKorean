package com.dailykorean.app.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.dailykorean.app.home.discover.DiscoverFragment
import com.dailykorean.app.home.more.MoreFragment
import com.dailykorean.app.home.my.MyFragment
import com.dailykorean.app.home.today.TodayFragment

/**
 * Created by musooff on 01/01/2019.
 */

class HomeViewPagerAdapter(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> TodayFragment()
            1 -> DiscoverFragment()
            2 -> MyFragment()
            3 -> MoreFragment()
            else -> TodayFragment()
        }
    }

    override fun getCount(): Int {
        return 4
    }
}