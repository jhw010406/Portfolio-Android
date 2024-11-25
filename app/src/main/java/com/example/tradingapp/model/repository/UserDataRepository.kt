package com.example.tradingapp.model.repository

import android.util.Log
import com.example.tradingapp.model.data.user.UserCertificate
import com.example.tradingapp.model.data.user.UserInformation
import com.example.tradingapp.model.repository.retrofit.LoginRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

// retrofit의 service 인터페이스
interface UserInfoRequest {
    // 아이디, 비밀번호 등 민감한 정보들을 body에 포함하여 보안성을 강화시키기 위해 POST 사용
    @POST("login")
    // Call<T> : Service 에서 반환 되는 객체(T)
    // Query : 서버 요청 시, 매개변수로 할당하는 추가적인 키
    fun LoginUser(
        @Body minimumUserData : UserCertificate
    ) : Call<UserInformation>

    // 데이터 등록
    @POST("register")
    fun RegisterUser(
        @Body userCertificate : UserCertificate
    ) : Call<UserInformation>
}

object UserDataRepository{

    // create : 주어진 service 기능을 수행하는 retrofit 객체 생성
    private val retrofit : UserInfoRequest = LoginRetrofit.retrofit.create(UserInfoRequest::class.java)

    fun loginUser(
        tag : String,
        id : String,
        password : String,
        callback : (UserCertificate, UserInformation?, responseCode : Int) -> Unit
    ) {
        val userCertificate = UserCertificate(id, password, false, null, null)
        val getUserInfo = retrofit.LoginUser(userCertificate)

        // enqueue : call<T>를 비동기 수행
        // Callback<T> : 서버 통신이 성공적으로 이뤄졌는지 확인하는 인터페이스
        // object : 클래스(또는 인터페이스) -> 무명 객체, 일회성으로 객체를 사용하는 방식
        getUserInfo.enqueue(object : Callback<UserInformation>{
            override fun onResponse(call: Call<UserInformation>, response: Response<UserInformation>) {
                if (response.isSuccessful){

                    response.headers()["Authorization"]?.let {

                        if (it.startsWith("Bearer ")) {
                            userCertificate.accessToken = it.substring(7)
                        }
                    }

                    response.headers()["Refresh-Token"]?.let {

                        if (it.startsWith("Bearer ")) {
                            userCertificate.refreshToken = it.substring(7)
                        }
                    }

                    if (userCertificate.accessToken == null || userCertificate.refreshToken == null) {
                        Log.e(tag, "access token or refresh token is null")
                        callback(userCertificate, null, response.code())

                        return
                    }

                    Log.d(tag, "login succeed" +
                            "\naccess token : ${userCertificate.accessToken}" +
                            "\nrefresh token : ${userCertificate.refreshToken}" +
                            "\n${response.body()}"
                    )
                    callback(userCertificate, response.body(), response.code())
                }
                else{
                    Log.d(tag, "server error : ${response.code()} ${response.message()}")
                    callback(userCertificate, null, response.code())
                }
            }

            override fun onFailure(call: Call<UserInformation>, t: Throwable) {
                Log.e(tag, "server connect failure", t)
                callback(userCertificate, null, 500)
            }
        })
    }

    fun RegisterUser(
        tag : String,
        userCertificate : UserCertificate,
        callback: (UserCertificate, UserInformation?, responseCode : Int) -> Unit
    ) {
        val createUserInfo = retrofit.RegisterUser(userCertificate)

        createUserInfo.enqueue(object : Callback<UserInformation>{
            override fun onResponse(call: Call<UserInformation>, response: Response<UserInformation>) {
                if (response.isSuccessful){

                    response.headers()["Authorization"]?.let {

                        if (it.startsWith("Bearer ")) {
                            userCertificate.accessToken = it.substring(7)
                        }
                    }

                    response.headers()["Refresh-Token"]?.let {

                        if (it.startsWith("Bearer ")) {
                            userCertificate.refreshToken = it.substring(7)
                        }
                    }

                    if (userCertificate.accessToken == null || userCertificate.refreshToken == null) {
                        Log.e(tag, "access token or refresh token is null")
                        callback(userCertificate, null, response.code())

                        return
                    }

                    Log.d(tag, "register success | ${response.body()} | ${response.body()?.uid}")
                    callback(userCertificate, response.body(), response.code())
                }
                else{
                    Log.d(tag, "error : ${response.code()} ${response.message()}")
                    callback(userCertificate, null, response.code())
                }
            }

            override fun onFailure(call: Call<UserInformation>, t: Throwable) {
                Log.d(tag, "server connect failure", t)
                callback(userCertificate, null, 500)
            }
        })
    }

    fun DeleteUser(){

    }

    fun UpdateUser(){

    }

}