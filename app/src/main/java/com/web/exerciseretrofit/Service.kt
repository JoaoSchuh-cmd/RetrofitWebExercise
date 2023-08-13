package com.web.exerciseretrofit

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET

interface Service {
    @GET("/")
    fun getData() : Call<ResponseBody>
}