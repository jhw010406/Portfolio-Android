package com.example.tradingapp.model.repository.retrofit

import com.example.tradingapp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object ImageBaseRetrofit {

    val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.STORAGE_ADDRESS)
        .client(
            OkHttpClient.Builder()
                .addInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                )
                .build()
        )
        .build()
}