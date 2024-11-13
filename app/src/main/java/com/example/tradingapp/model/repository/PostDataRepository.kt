package com.example.tradingapp.model.repository

import android.util.Log
import com.example.tradingapp.model.data.post.PostDetails
import com.example.tradingapp.model.repository.retrofit.BaseRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PostDataRequest{

    @GET("post")
    fun getPostDetails(
        @Query("post-id") postId : Int
    ) : Call<PostDetails>

    @POST("post")
    // 아무 것도 반환 받지 않으므로, 반환 자료형은 Unit으로 한다
    fun uploadPost(
        @Body postDetails : PostDetails
    ) : Call<Unit>

    @GET("posts-list/{category}")
    fun getPreviewPostsList(
        @Path("category") postCategory : Int,
        @Query("user-uid") userUID : Int? = null,
        @Query("page-number") startPageNumber : Int,
        @Query("page-count") pageCount : Int
    ) : Call<List<PostDetails>>

    @GET("favorite-posts-list")
    fun getPreviewFavoritePostsList(
        @Query("user-uid") userUid : Int,
        @Query("page-number") startPageNumber : Int,
        @Query("page-count") pageCount : Int
    ) : Call<List<PostDetails>>

    @GET("favorite-post")
    fun isFavoritedPost(
        @Query("user-uid") userUid: Int,
        @Query("post-id") postId: Int
    ) : Call<Boolean>

    @POST("favorite-post")
    fun favoritePost(
        @Query("user-uid") userUid : Int,
        @Query("post-id") postId : Int
    ) : Call<Unit>

    @DELETE("favorite-post")
    fun cancelFavoritePost(
        @Query("user-uid") userUid : Int,
        @Query("post-id") postId : Int
    ) : Call<Unit>
}

object PostDataRepository {

    val retrofit : PostDataRequest = BaseRetrofit.retrofit.create(PostDataRequest::class.java)


    fun getPostDetails(
        tag : String,
        inputPostId : Int,
        callback : (PostDetails?, Boolean) -> Unit
    ) {
        val getPostDetails = retrofit.getPostDetails(inputPostId)

        getPostDetails.enqueue(object : Callback<PostDetails>{
            override fun onResponse(call: Call<PostDetails>, response: Response<PostDetails>) {

                if (response.isSuccessful){
                    Log.d(tag, "get post details succeed")
                    callback(response.body(), true)
                }
                else{
                    Log.d(tag, "server error : ${response.code()}")
                    callback(null, false)
                }
            }

            override fun onFailure(call: Call<PostDetails>, t: Throwable) {
                Log.e(tag, "server connect failure", t)
                callback(null, false)
            }
        })
    }


    fun uploadPost(
        inputPostDetails : PostDetails,
        callback : (Int) -> Unit
    ){
        val getPostDetails = retrofit.uploadPost(inputPostDetails)

        getPostDetails.enqueue(object : Callback<Unit>{
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful){
                    Log.d("POST", "upload post succeed")
                    callback(response.code())
                }
                else{
                    Log.d("POST", "upload post failed : ${response.code()} ${response.message()}")
                    callback(response.code())
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Log.e("POST", "server connect failure", t)
                callback(500)
            }
        })
    }


    fun getPreviewPostsList(
        tag : String,
        inputUserUid : Int?,
        inputPostCategory : Int,
        inputStartPageNumber :Int,
        inputPageCount : Int,
        isAboutFavorite : Boolean,
        callback: (List<PostDetails>?, Int) -> Unit
    ){
        val getPostsList : Call<List<PostDetails>>

        if (isAboutFavorite) {

            if (inputUserUid == null){
                Log.e(tag, "user uid is null")
                callback(null, 400)
                return
            }
            getPostsList = retrofit.getPreviewFavoritePostsList(inputUserUid, inputStartPageNumber, inputPageCount)
        }
        else {
            getPostsList = retrofit.getPreviewPostsList(inputPostCategory, inputUserUid, inputStartPageNumber, inputPageCount)
        }

        getPostsList.enqueue(object : Callback<List<PostDetails>>{
            override fun onResponse(
                call: Call<List<PostDetails>>,
                response: Response<List<PostDetails>>
            ) {
                if (response.isSuccessful){
                    Log.d(tag, "get posts list succeed")
                    callback(response.body(), response.code())
                }
                else{
                    Log.d(tag, "get posts failed : ${response.code()} ${response.message()}")
                    callback(null, response.code())
                }
            }

            override fun onFailure(call: Call<List<PostDetails>>, t: Throwable) {
                Log.e(tag, "server connect failure", t)
                callback(null, 500)
            }
        })
    }

    fun isFavoritedPost(
        tag: String,
        inputUserUid: Int,
        inputPostId: Int,
        callback: (Boolean?) -> Unit
    ) {
        val isFavoritedPost = retrofit.isFavoritedPost(inputUserUid, inputPostId)

        isFavoritedPost.enqueue(object : Callback<Boolean>{
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {

                if (response.isSuccessful) {
                    Log.d(tag, "checked ${inputPostId} is favorited post : ${response.body()}")
                    callback(response.body())
                }
                else {
                    Log.e(tag, "server error ${response.code()} ${response.message()}")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Log.e(tag, "server connect failure")
                callback(null)
            }
        })
    }

    fun favoritePost(
        tag : String,
        inputUserUid : Int,
        inputPostId : Int,
        callback : (Boolean) -> Unit
    ) {
        val favoritePost = retrofit.favoritePost(inputUserUid, inputPostId)

        favoritePost.enqueue(object : Callback<Unit>{
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {

                if (response.isSuccessful) {
                    Log.d(tag, "${inputUserUid} favorites ${inputPostId}")
                    callback(true)
                }
                else {
                    Log.e(tag, "server error ${response.code()} ${response.message()}")
                    callback(false)
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Log.e(tag, "server connect failure", t)
                callback(false)
            }
        })
    }

    fun unfavoritePost(
        tag : String,
        inputUserUid : Int,
        inputPostId : Int,
        callback : (Boolean) -> Unit
    ) {
        val cancelFavoritePost = retrofit.cancelFavoritePost(inputUserUid, inputPostId)

        cancelFavoritePost.enqueue(object : Callback<Unit>{
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {

                if (response.isSuccessful) {
                    Log.d(tag, "${inputUserUid} cancel to favorite about ${inputPostId}")
                    callback(true)
                }
                else {
                    Log.e(tag, "server error ${response.code()} ${response.message()}")
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