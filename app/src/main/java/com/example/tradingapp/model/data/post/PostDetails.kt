package com.example.tradingapp.model.data.post

import android.os.Parcelable
import com.example.tradingapp.model.data.Address
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

enum class PostCategories(
    val value : Int
){
    TRADING(0),
    COMMUNITY(1)
}

@Parcelize
data class PostDetails(
    // post id
    val postID : Int,
    val posterUID : Int?,
    val posterID : String?,
    val category : Int?,
    @Expose(serialize = false) val thumbnailImageUrl : String?,
    // post title
    val title : String,
    // post content
    val content : String?,
    // The count how many people saw
    val viewCount : Int?,
    // uploaded date
    // 전송하는 json에 포함시키지 않는다.
    @Expose(serialize = false) val uploadDate : String?,
    // last modified date
    @Expose(serialize = false) val modifyDate : String?,
    // The location for trading or communicating
    val location : Address?,
    val postDetailsTrading: PostDetailsTrading?,
    val postDetailsCommunity: PostDetailsCommunity?
) : Parcelable