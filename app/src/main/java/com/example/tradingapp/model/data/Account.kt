package com.example.tradingapp.model.data

data class Account(
    val id : String,
    val password : String,
    val name : String,
    val age : Int,
    val phoneNumber : String,
    val address : Address,
    val profilePicture : String,
    val nickName : String,
    // The list of the posts that I written
    val posts : List<Int>,
    // The chat IDs that I am participating in
    val chats : List<Int>,
    // The list of the posts that I favorite, Int -> post ID
    val favorites : List<Int>,
    val setting : AccountSetting
)

data class AccountSetting(
    val blockReceivingNotification : Boolean
)