package com.example.tradingapp.viewmodel.post

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import com.example.tradingapp.model.data.post.PostDetails
import com.example.tradingapp.model.repository.PostDataRepository
import java.time.LocalDateTime


fun getSelectedPostDetails(
    tag : String,
    inputPostId : Int,
    callback : (PostDetails?, Boolean) -> Unit
) {
    PostDataRepository.getPostDetails(tag, inputPostId){ getPostDetails, isSuccessful ->
        if (isSuccessful){

            if (getPostDetails == null)
                callback(null, false)

            Log.d(tag, "[ selected post details ]  ${getPostDetails}")
            callback(getPostDetails, true)
        }
        else{
            callback(null, false)
        }
    }
}


fun getPostsList(
    tag : String,
    inputPostCategory : Int,
    inputStartPageNumber : Int,
    inputPageCount : Int,
    inputUserUid : Int?,
    inputPostsList : MutableList<PostDetails>,
    isAboutFavorite : Boolean,
    callback : (Boolean) -> Unit
) {
    var listSize : Int
    Log.d(tag, "poster uid : ${inputUserUid}")

    PostDataRepository.getPreviewPostsList(
        tag,
        inputUserUid,
        inputPostCategory,
        inputStartPageNumber,
        inputPageCount,
        isAboutFavorite
    ){ postDetailsList, responseCode ->
        when (responseCode / 100) {
            2 -> {
                if (postDetailsList != null){
                    listSize = postDetailsList.size
                    Log.d(tag, "gotten post details list's size = ${postDetailsList.size}")

                    for (idx : Int in 0..listSize - 1){
                        Log.d(tag, "- post details - \n${postDetailsList.get(idx)}")
                        inputPostsList.add(postDetailsList.get(idx))
                    }
                }
                callback(true)
            }
            else -> {

            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
fun getTimeLagFromNow(
    inputDateTime : LocalDateTime
) : String {
    val currentDateTime = LocalDateTime.now()

    if (inputDateTime.year == currentDateTime.year){
        if (inputDateTime.monthValue == currentDateTime.monthValue){
            if (inputDateTime.dayOfMonth == currentDateTime.dayOfMonth){
                if (inputDateTime.hour == currentDateTime.hour){
                    return "${currentDateTime.minute - inputDateTime.minute}분"
                }
                else{
                    return "${currentDateTime.hour - inputDateTime.hour}시간"
                }
            }
            else{
                if ((inputDateTime.dayOfMonth - currentDateTime.dayOfMonth) / 7 > 0){
                    return "${(currentDateTime.dayOfMonth - inputDateTime.dayOfMonth) / 7}주"
                }
                else{
                    return "${currentDateTime.dayOfMonth - inputDateTime.dayOfMonth}일"
                }
            }
        }
        else{
            return "${inputDateTime.monthValue - currentDateTime.monthValue}달"
        }
    }
    else{
        return "${inputDateTime.year - currentDateTime.year}년"
    }
}


fun getAlphaValueForHeader(
    firstVisibleItemIndex : Int,
    firstVisibleItemScrollOffset : Int
) : Float {
    if (firstVisibleItemIndex != 0){ return (1f) }

    if ((firstVisibleItemScrollOffset / 0xFF) >= 3){ return (1f) }
    else { return (firstVisibleItemScrollOffset.toFloat() / (0xFF * 3)) }
}


fun deletePost(
    tag : String,
    inputPostId: Int,
    callback: (Boolean) -> Unit
) {
    PostDataRepository.deletePost(tag, inputPostId) { isSucessful ->
        callback(isSucessful)
    }
}


fun favoritePost(
    tag : String,
    inputUserUid : Int,
    inputPostId: Int,
    callback : (Boolean) -> Unit
) {
    PostDataRepository.favoritePost(tag, inputUserUid, inputPostId) { isSuccessful ->
        callback(isSuccessful)
    }
}


fun unfavoritePost(
    tag : String,
    inputUserUid : Int,
    inputPostId: Int,
    callback : (Boolean) -> Unit
) {
    PostDataRepository.unfavoritePost(tag, inputUserUid, inputPostId) { isSuccessful ->
        callback(isSuccessful)
    }
}