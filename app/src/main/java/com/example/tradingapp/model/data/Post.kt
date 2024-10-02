package com.example.tradingapp.model.data

import android.media.Image

data class Post(
    // post id
    val id : Int,
    // writer ID
    val poster : String,
    // post title
    val title : String,
    // post content
    val content : String,
    // posted time
    val postTime : String,
    // The location for trading or communicating
    val location : Address?,
    // The count how many people saw
    val viewCount : Int,
    val isForTrading : Boolean,
    val othersForTrading: OthersForTrading?,
    val othersForCommunity: OthersForCommunity?
)

data class OthersForTrading(
    val productImage: Image,
    val productType : String,
    val price : Int,
    // 중고차 연식
    val modelYear : String?,
    // 중고차 주행 거리
    val mileage : String?,
    // 문의 중인 수
    val chattingCount : String,
    // 찜 횟수
    val favoritesCount : Int
)

data class OthersForCommunity(
    val subject : String,
    val isTrendingPost : Boolean,
    val comments : List<Comment>?
)