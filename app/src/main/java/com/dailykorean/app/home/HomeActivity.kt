package com.dailykorean.app.home

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import com.dailykorean.app.R
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        home_vp.adapter = HomeViewPagerAdapter(supportFragmentManager)

    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_today -> {
                home_vp.currentItem = 0
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_discover -> {
                home_vp.currentItem = 1
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_more -> {
                home_vp.currentItem = 2
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_my -> {
                home_vp.currentItem = 3
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
}
