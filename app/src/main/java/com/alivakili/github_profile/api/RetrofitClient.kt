package com.alivakili.github_profile.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {
    private const val baseUrl="https://api.github.com/"
    val retrofit:Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    object ApiClient {
        val apiService: Endpoint by lazy {
            retrofit.create(Endpoint::class.java)
        }
    }



}