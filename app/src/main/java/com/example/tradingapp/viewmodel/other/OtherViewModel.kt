package com.example.tradingapp.viewmodel.other

import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts

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