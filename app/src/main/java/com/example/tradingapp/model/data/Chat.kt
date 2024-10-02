package com.example.tradingapp.model.data

data class Chat(
    val id : Int,
    val title : String,
    // If the chat is for trading, then it is private
    val isPrivate : Boolean,
    val members : List<String>?,
    val maxParticipant : Int,
    val messages : List<Message>?
)

data class Message(
    // 발신자
    val sender : String,
    // 메세지 내용
    val content : String,
    // 발신 시각
    val sendTime : String
)