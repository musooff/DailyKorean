package com.dailykorean.app.home.today

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dailykorean.app.R

/**
 * Created by musooff on 01/01/2019.
 */

class TodayFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_today, container, false)
        return view
    }
}