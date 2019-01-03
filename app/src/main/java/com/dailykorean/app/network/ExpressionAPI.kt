package com.dailykorean.app.network

import com.dailykorean.app.main.discover.model.ExpressionResult
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path


/**
 * Created by musooff on 01/01/2019.
 */

interface ExpressionAPI {
    @GET("{date}/conversation.dict")
    fun getUsers(@Path("date") date: String): Observable<ExpressionResult>
}