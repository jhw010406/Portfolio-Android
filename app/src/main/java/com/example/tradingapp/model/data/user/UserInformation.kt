package com.example.tradingapp.model.data.user

import com.example.tradingapp.model.data.comment.CommentDetails
import com.example.tradingapp.model.data.post.PostDetails
import com.google.gson.annotations.Expose

data class UserInformation(
    val uid : Int,
    var id : String,
    val password : String,
    val nickname : String?,
    val age : Int?,
    val phoneNumber : String?,
    val postsList : List<PostDetails>?,
    val commentsList : List<CommentDetails>?
)

