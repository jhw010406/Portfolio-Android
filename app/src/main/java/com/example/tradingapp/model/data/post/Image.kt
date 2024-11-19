package com.example.tradingapp.model.data.post

import android.os.Parcelable
import com.example.tradingapp.model.data.request.RequestBodyFromContentURI
import kotlinx.parcelize.Parcelize

data class Image(
    var imageNumber : Int = -1,

    var name : String = "",

    val contentType : String? = null,

    var preSignedUrl : String = "",

    var url : String = "",

    var file : RequestBodyFromContentURI? = null,

    @Transient
    var loading : Boolean = false,

    @Transient
    val postId : Int = -1
)

@Parcelize
data class ImageForGet(
    var imageNumber: Int,

    var name : String,

    var url : String
) : Parcelable

@Parcelize
data class ImageForUpdate(
    val postId: Int,

    var name: String,

    var url: String,

    val imageNumber: Int
) : Parcelable