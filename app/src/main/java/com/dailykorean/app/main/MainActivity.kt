package com.dailykorean.app.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import com.dailykorean.app.R
import com.dailykorean.app.main.discover.DiscoverFragment
import com.dailykorean.app.main.more.MoreFragment
import com.dailykorean.app.main.my.MyFragment
import com.dailykorean.app.main.home.HomeFragment
import kotlinx.android.synthetic.main.activity_home.*
import androidx.fragment.app.FragmentManager
import com.dailykorean.app.common.base.BaseActivity


class MainActivity : BaseActivity() {

    companion object {
        private const val HOME_FRAGMENT_TAG = "homeFragment"
        private const val DISCOVER_FRAGMENT_TAG = "discoverFragment"
        private const val MY_FRAGMENT_TAG = "myFragment"
        private const val MORE_FRAGMENT_TAG = "moreFragment"

        fun newIntent(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var mFragmentManager: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        mFragmentManager = supportFragmentManager
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        loadFragment(HomeFragment())

    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                loadFragment(HomeFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_discover -> {
                loadFragment(DiscoverFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_my -> {
                loadFragment(MyFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_more -> {
                loadFragment(MoreFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commitNow()
    }

    private fun switchFragment(tag: String){
        val fragmentTransaction = mFragmentManager.beginTransaction()
        val curFrag = mFragmentManager.primaryNavigationFragment
        if (curFrag != null) {
            fragmentTransaction.detach(curFrag)
        }

        var fragment = mFragmentManager.findFragmentByTag(tag)
        if (fragment == null) {
            fragment = getFragment(tag)
            fragmentTransaction.add(R.id.fragment_container, fragment, tag)
        } else {
            fragmentTransaction.attach(fragment)
        }

        fragmentTransaction.setPrimaryNavigationFragment(fragment)
        fragmentTransaction.setReorderingAllowed(true)
        fragmentTransaction.commitNow()
    }

    private fun getFragment(tag: String): Fragment{
        return when(tag){
            HOME_FRAGMENT_TAG -> HomeFragment()
            DISCOVER_FRAGMENT_TAG -> DiscoverFragment()
            MY_FRAGMENT_TAG -> MyFragment()
            MORE_FRAGMENT_TAG -> MoreFragment()
            else -> HomeFragment()
        }
    }
}
