package com.dailykorean.app.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dailykorean.app.R
import com.dailykorean.app.dialog.ErrorDialog
import com.dailykorean.app.main.MainActivity
import com.dailykorean.app.utils.Ln

/**
 * Created by musooff on 03/01/2019.
 */

class SplashActivity : AppCompatActivity() {

    private lateinit var repository: SplashRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        repository = SplashRepository(this)

        checkAndStart()
    }

    private fun checkAndStart() {
        repository.updateDatabase()
                .subscribe({
                    if (it) {
                        MainActivity.newIntent(this)
                        this.finish()
                    } else {
                        ErrorDialog.noInternet(this) {checkAndStart()}
                    }
                }, { Ln.e(it) })
    }
}