package com.example.tradingapp.model.data.post

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostDetailsTrading(
    // pair<filename, image_index>
    val productImagesForUpload : List< Pair<String, Int> >? = null,
    val productImagesForUpdate : List<ImageForUpdate>? = null,
    // pair<image_url, image_index>
    val productImagesForGet : List<ImageForGet>? = null,
    val productType : String? = null,
    val productPrice : Int,
    // 중고차 연식
    val modelYear : Int? = null,
    // 중고차 주행 거리
    val mileage : Int? = null,
    // 문의 중인 수
    val chatCount : Int = 0,
    // 찜 된 횟수
    val favoriteCount : Int = 0
) : Parcelable