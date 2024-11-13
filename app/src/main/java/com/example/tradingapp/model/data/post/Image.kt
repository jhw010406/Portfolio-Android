package com.example.tradingapp.model.data.post

import com.example.tradingapp.model.data.request.RequestBodyFromContentURI

data class Image(
    var imageNumber : Int,

    var name : String,

    val contentType : String?,

    var preSignedUrl : String,

    var url : String,

    var file : RequestBodyFromContentURI,

    @Transient
    var loading : Boolean,

    @Transient
    val postId : Int
)
