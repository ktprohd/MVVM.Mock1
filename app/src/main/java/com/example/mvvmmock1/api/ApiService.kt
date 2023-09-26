package com.example.mvvmmock1.api

import retrofit.Call
import retrofit.http.GET
import com.example.mvvmmock1.model.DataModel

interface ApiService {
    @GET("jokes/random")
    fun getjokes(): Call<DataModel>

}