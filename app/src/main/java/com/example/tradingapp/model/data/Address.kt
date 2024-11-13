package com.example.tradingapp.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address (
    // -도
    val state : String?,
    // -시
    val city : String?,
    // -군, -구
    val town : String?,
    // -동, -읍
    val townShip : String?,
    // -면, -리
    val village : String?
) : Parcelable