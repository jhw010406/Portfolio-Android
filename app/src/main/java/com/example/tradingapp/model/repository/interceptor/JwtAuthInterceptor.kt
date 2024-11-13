package com.example.tradingapp.model.repository.interceptor

import android.util.Log
import com.example.tradingapp.model.repository.local.LocalUserCertificateRepository
import com.example.tradingapp.model.repository.local.LocalUserCertificateGraph
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.net.HttpURLConnection

class JwtAuthInterceptor (
    private val repository: LocalUserCertificateRepository = LocalUserCertificateGraph.localUserCertificateRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val tag = "JWT_Authorization"
        // runBlocking : 코루틴이 종료될 때까지 코루틴을 호출한 스레드를 중지 시킨다.
        var accessToken : String? = runBlocking { repository.getAccessToken()?.first() }
        var refreshToken : String?
        var responseWithAT : Response
        var responseWithRT : Response
        var responseAccessToken : String?
        var responseRefreshToken : String?
        val baseRequest : Request = chain.request()
        // AT = Access Token
        var requestWithAT : Request
        // RT = Refresh Token
        var requestWithRT : Request

        Log.d(tag, "access token : ${accessToken}")
        accessToken?.let { aT ->
            requestWithAT = baseRequest.newBuilder()
                .header("Authorization", "Bearer $aT")
                .build()
            responseWithAT = chain.proceed(requestWithAT)

            // if access denied (access token is expired)
            if (responseWithAT.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
                responseWithAT.close()
                refreshToken = runBlocking { repository.getRefreshToken()?.first() }

                refreshToken?.let { rT ->
                    requestWithRT = baseRequest.newBuilder()
                        .header("Refresh-Token", "Bearer $rT")
                        .build()
                    responseWithRT = chain.proceed(requestWithRT)
                    responseAccessToken = responseWithRT.header("Authorization")
                    responseRefreshToken = responseWithRT.header("Refresh-Token")

                    Log.d(tag, "new access token : ${responseAccessToken}\nnew refresh token : ${responseRefreshToken}")
                    if (responseWithRT.code != HttpURLConnection.HTTP_OK ||
                        responseAccessToken == null ||
                        responseRefreshToken == null
                    ) { return responseWithRT }

                    if (responseAccessToken!!.startsWith("Bearer ")) {
                        CoroutineScope(Dispatchers.IO).launch {
                            repository.updateAccessToken(responseAccessToken!!.substring(7))
                        }
                    }
                    else {
                    }

                    if (responseRefreshToken!!.startsWith("Bearer ")) {
                        CoroutineScope(Dispatchers.IO).launch {
                            repository.updateRefreshToken(responseRefreshToken!!.substring(7))
                        }
                    }
                    else {
                    }

                    responseWithRT.close()

                    return chain.proceed(
                        baseRequest.newBuilder()
                            .header("Authorization", responseAccessToken!!)
                            .build()
                    )
                }
            }
            else {
                return responseWithAT
            }
        }

        return chain.proceed(chain.request())
    }
}