package com.dailykorean.app.common.base

import androidx.appcompat.app.AppCompatActivity
import com.dailykorean.app.utils.PermissionUtils
import kotlinx.android.synthetic.main.toolbar.*

/**
 * Created by musooff on 02/01/2019.
 */

abstract class BaseActivity : AppCompatActivity() {

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    protected open fun initToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}