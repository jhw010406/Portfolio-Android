package com.example.tradingapp.model.data.post

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostDetailsTrading(
    // pair<filename, image_index>
    val productImagesForUpload : List< Pair<String, Int> >?,
    // pair<image_url, image_index>
    val productImagesForGet : List< Pair<String, Int> >?,
    val productType : String?,
    val productPrice : Int,
    // 중고차 연식
    val modelYear : Int?,
    // 중고차 주행 거리
    val mileage : Int?,
    // 문의 중인 수
    val chatCount : Int,
    // 찜 된 횟수
    val favoriteCount : Int
) : Parcelable