package com.dailykorean.app.ad

import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView


/**
 * Created by musooff on 05/01/2019.
 */

class AdManager() {

    fun loadAdBannerAd(mAdView : AdView){

        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }
}