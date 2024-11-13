package com.example.tradingapp.model.data.comment

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class CommentDetails(
    val id : Int,
    val content : String,
    val uploadDate : LocalDateTime,
    val modifyDate : LocalDateTime?
) : Parcelable