package com.example.tradingapp.model.repository.retrofit

import com.example.tradingapp.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object LoginRetrofit {

    val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.SERVER_ADDRESS)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}