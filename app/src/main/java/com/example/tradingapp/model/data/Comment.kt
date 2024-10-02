package com.example.tradingapp.model.data

data class Comment(
    // writer ID
    val writer : String,
    val content : String,
    val writeTime : String,
    val likeCount : Int,
    val replies : List<reply>?
)

data class reply(
    // writer ID
    val writer : String,
    val content : String,
    val writeTime : String,
    val likeCount : Int
)
