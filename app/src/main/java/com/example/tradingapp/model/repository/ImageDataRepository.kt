package com.example.tradingapp.model.repository

import android.util.Log
import com.example.tradingapp.model.data.post.Image
import com.example.tradingapp.model.repository.retrofit.BaseRetrofit
import com.example.tradingapp.model.repository.retrofit.ImageBaseRetrofit
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query
import retrofit2.http.Url

interface ImageRequestToServer {
    @GET("post-image")
    fun getImagePreSignedURL(
        @Query("post-id") postId: Int,
        @Query("filename") filename : String,
        @Query("contentType") contentType : String
    ) : Call<Image>

    @DELETE("post-image")
    fun deletePostImage(
        @Query("filename") filename : String
    ) : Call<Unit>
}

interface ImageRequestToStorage {
    @PUT
    fun putImageData(
        @Url preSignedURL : String,
        @Body imageFile : RequestBody
    ) : Call<Unit>
}

object ImageDataRepository{

    private val serverRetrofit: ImageRequestToServer = BaseRetrofit.retrofit.create(ImageRequestToServer::class.java)
    private val storageRetrofit: ImageRequestToStorage = ImageBaseRetrofit.retrofit.create(ImageRequestToStorage::class.java)

    fun getImagePreSignedURL (
        tag : String,
        inputPostId : Int,
        inputFilename : String,
        inputContentType : String,
        callback : (Image?, Boolean) -> Unit
    ) {
        val getPreSignedURL = serverRetrofit.getImagePreSignedURL(inputPostId, inputFilename, inputContentType)

        getPreSignedURL.enqueue(object : Callback<Image>{
            override fun onResponse(call: Call<Image>, response: Response<Image>) {

                if (response.isSuccessful){
                    Log.d(tag, "get pre-signed url : get response : ${response.body()}")
                    callback(response.body(), true)
                }
                else{
                    Log.d(tag, "get pre-signed url : server error : ${response.code()}")
                    callback(null, false)
                }
            }

            override fun onFailure(call: Call<Image>, t: Throwable) {
                Log.e(tag, "get pre-signed url : server connect failure", t)
                callback(null, false)
            }
        })
    }

    fun putImage (
        tag : String,
        inputImage : Image,
        callback : (Boolean) -> Unit
    ) {
        val putImageData = storageRetrofit.putImageData(
            preSignedURL = inputImage.preSignedUrl,
            imageFile = inputImage.file!!
        )
        Log.d(tag, inputImage.preSignedUrl)

        putImageData.enqueue(object : Callback<Unit>{
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {

                if (response.isSuccessful){
                    Log.d(tag, "upload image : get response : ${response.code()}")
                    callback(true)
                }
                else {
                    Log.e(tag, "upload image : server error : ${response.code()}")
                    callback(false)
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Log.e(tag, "upload image : server connect failure", t)
                callback(false)
            }
        })
    }

    fun deletePostImage(
        tag : String,
        inputFilename : String,
        callback: (Boolean) -> Unit
    ) {
        val deletePostImage = serverRetrofit.deletePostImage(inputFilename)

        deletePostImage.enqueue(object : Callback<Unit>{
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {

                if (response.isSuccessful){
                    Log.d(tag, "delete ${inputFilename} succeed")
                    callback(true)
                }
                else{
                    Log.e(tag, "server error : ${response.code()}")
                    callback(false)
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Log.e(tag, "server connect failure", t)
                callback(false)
            }
        })
    }
}