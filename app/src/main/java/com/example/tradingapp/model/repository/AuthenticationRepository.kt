package com.example.tradingapp.model.repository

import android.util.Log
import com.example.tradingapp.model.repository.retrofit.BaseRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface AuthRequest {

    @GET("/")
    fun getAccessToken(
        @Header("Refresh-Token") refreshToken : String
    ) : Call<Unit>
}

object AuthenticationRepository {

    val retrofit = BaseRetrofit.retrofit.create(AuthRequest::class.java)

    fun getAccessToken(
        tag : String = "GET_ACCESS_TOKEN",
        inputRefreshToken : String,
        callback : (String?, Boolean) -> Unit
    ) {
        val getAccessToken = retrofit.getAccessToken(inputRefreshToken)

        getAccessToken.enqueue(object : Callback<Unit>{
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {

                if (response.isSuccessful) {
                    Log.d(tag, "get new access token successfully\naccess token : ${response.headers().get("Authorization")}")
                    callback(response.headers().get("Authorization"), true)
                }
                else {
                    Log.e(tag, "server error : ${response.code()}")
                    callback(null, false)
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Log.e(tag, "server connect failure", t)
            }
        })
    }
}