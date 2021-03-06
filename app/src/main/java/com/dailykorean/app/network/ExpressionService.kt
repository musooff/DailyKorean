package com.dailykorean.app.network

import com.dailykorean.app.main.discover.model.ExpressionResult
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

/**
 * Created by musooff on 01/01/2019.
 */

class ExpressionService {
    companion object {
        private const val SERVER_BASE_URL = "http://dict-channelgw.naver.com/endic/kr/enko/today/"
    }

    private fun startService(): ExpressionAPI {

        val retrofit = Retrofit.Builder()
                .baseUrl(SERVER_BASE_URL)
                .addConverterFactory(JacksonConverterFactory
                        .create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()

        return retrofit.create(ExpressionAPI::class.java)

    }

    fun getExpression(date: String) : Observable<ExpressionResult> {
        return startService().getUsers(date)
    }
}