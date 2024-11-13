package com.example.tradingapp.model.viewmodel.other

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.room.util.getColumnIndex
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okio.BufferedSink
import okio.source
import okio.use
import java.io.File
import java.io.InputStream

fun addDelimiterToPrice(
    input : String
) : String {
    val inputLength = input.length
    var result : String = ""

    if ((inputLength == 0) || (input.equals("0"))){
        return result
    }

    for (idx : Int in 0..inputLength - 1){
        if (
            (inputLength - idx) % 3 == 0 &&
            idx != 0 &&
            idx < inputLength - 1
        ){
            result += ','
        }

        result += input[idx]
    }

    return result
}

fun selectOptimalContractForMedia(
    currentImagesCount : Int
) : ActivityResultContract<PickVisualMediaRequest, *> {

    val remainder = 10 - currentImagesCount

    return  if (remainder > 1) ActivityResultContracts.PickMultipleVisualMedia(remainder)
            else ActivityResultContracts.PickVisualMedia()
}