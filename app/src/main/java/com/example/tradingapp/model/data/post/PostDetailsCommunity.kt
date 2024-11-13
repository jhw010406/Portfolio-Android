package com.example.tradingapp.model.data.post

import android.os.Parcelable
import com.example.tradingapp.model.data.comment.CommentDetails
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostDetailsCommunity(
    val subject : String,
    val isTrendingPost : Boolean,
    val commentsList : List<CommentDetails>?
) : Parcelable