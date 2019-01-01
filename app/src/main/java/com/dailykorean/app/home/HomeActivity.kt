package com.dailykorean.app.home

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dailykorean.app.R
import com.dailykorean.app.home.discover.DiscoverFragment
import com.dailykorean.app.home.more.MoreFragment
import com.dailykorean.app.home.my.MyFragment
import com.dailykorean.app.home.today.TodayFragment
import kotlinx.android.synthetic.main.activity_home.*
import androidx.fragment.app.FragmentManager


class HomeActivity : AppCompatActivity() {

    companion object {
        private const val TODAY_FRAGMENT_TAG = "todayFragment"
        private const val DISCOVER_FRAGMENT_TAG = "discoverFragment"
        private const val MY_FRAGMENT_TAG = "myFragment"
        private const val MORE_FRAGMENT_TAG = "moreFragment"
    }

    private lateinit var mFragmentManager: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        mFragmentManager = supportFragmentManager
        loadFragment(TodayFragment())
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_today -> {
                switchFragment(TODAY_FRAGMENT_TAG)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_discover -> {
                switchFragment(DISCOVER_FRAGMENT_TAG)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_my -> {
                switchFragment(MY_FRAGMENT_TAG)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_more -> {
                switchFragment(MORE_FRAGMENT_TAG)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun loadFragment(fragment: Fragment?): Boolean {
        if (fragment != null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit()
            return true
        }
        return false
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
            fragmentTransaction.add(container.id, fragment, tag)
        } else {
            fragmentTransaction.attach(fragment)
        }

        fragmentTransaction.setPrimaryNavigationFragment(fragment)
        fragmentTransaction.setReorderingAllowed(true)
        fragmentTransaction.commitNowAllowingStateLoss()
    }

    private fun getFragment(tag: String): Fragment{
        return when(tag){
            TODAY_FRAGMENT_TAG -> TodayFragment()
            DISCOVER_FRAGMENT_TAG -> DiscoverFragment()
            MY_FRAGMENT_TAG -> MyFragment()
            MORE_FRAGMENT_TAG -> MoreFragment()
            else -> TodayFragment()
        }
    }
}
