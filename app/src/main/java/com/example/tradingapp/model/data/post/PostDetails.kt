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
    val postID : Int = 0,
    val posterUID : Int? = null,
    val posterID : String? = null,
    val category : Int? = null,
    @Expose(serialize = false) val thumbnailImageUrl : String? = null,
    // post title
    val title : String,
    // post content
    val content : String? = null,
    // The count how many people saw
    val viewCount : Int? = null,
    // uploaded date
    // 전송하는 json에 포함시키지 않는다.
    @Expose(serialize = false) val uploadDate : String? = null,
    // last modified date
    @Expose(serialize = false) val modifyDate : String? = null,
    // The location for trading or communicating
    val location : Address? = null,
    val postDetailsTrading: PostDetailsTrading? = null,
    val postDetailsCommunity: PostDetailsCommunity? = null
) : Parcelable