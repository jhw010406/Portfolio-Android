package com.example.tradingapp.model.data.request

data class RequestPost(
    val userUID : Int?,
    val postCategory : Int?,
    val startNumber : Int,
    val postsCount : Int
)
