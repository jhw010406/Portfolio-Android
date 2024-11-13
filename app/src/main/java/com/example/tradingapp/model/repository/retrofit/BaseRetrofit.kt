package com.example.tradingapp.model.repository.retrofit

import com.example.tradingapp.BuildConfig
import com.example.tradingapp.model.repository.interceptor.JwtAuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object BaseRetrofit {

    // Builder : builder 인스턴스를 생성하고, 옵션을 정함
    // baseUrl : 기본 url, 해당 url 뒤에 요청 값들을 이어 붙임
    // addConverterFactory : 데이터 송수신 시, 어떤 format 으로 mapping 할 것인지 정함
    // build : retrofit 인스턴스 생성
    val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.SERVER_ADDRESS)
        .addConverterFactory(GsonConverterFactory.create())
        .client(
            OkHttpClient.Builder()
                .addInterceptor(
                    JwtAuthInterceptor()
                )
                .build()
        )
        .build()

}