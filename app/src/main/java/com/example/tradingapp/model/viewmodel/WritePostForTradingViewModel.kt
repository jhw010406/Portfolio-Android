package com.example.tradingapp.model.viewmodel

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel

class WritePostForTradingViewModel(
    val postTitle: MutableState<String> = mutableStateOf(""),
    val productPrice: MutableState<String> = mutableStateOf(""),
    val productDetails: MutableState<String> = mutableStateOf(""),
    val tradingLocation: MutableState<String> = mutableStateOf(""),
    val forSelling: MutableState<Boolean> = mutableStateOf(true),
    val acceptOtherPriceRequest: MutableState<Boolean> = mutableStateOf(false),
    val acceptFreebieRequest: MutableState<Boolean> = mutableStateOf(false),
    val selectedImages: SnapshotStateList<Uri?> = mutableStateListOf()
) : ViewModel(){
    fun UploadPostForTrading(){

    }
}