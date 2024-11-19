package com.example.tradingapp.model.viewmodel.post

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalContext
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import coil.compose.AsyncImage
import com.example.tradingapp.R
import com.example.tradingapp.model.data.navigation.MainNavigationGraph
import com.example.tradingapp.model.data.post.PostCategories
import com.example.tradingapp.model.data.post.PostDetails
import com.example.tradingapp.model.repository.PostDataRepository
import com.example.tradingapp.model.viewmodel.other.addDelimiterToPrice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
) : Long {
    var offset : Int

    if (firstVisibleItemIndex != 0){ return (0xFF000000) }

    if ((firstVisibleItemScrollOffset / 3) >= 0xFF){ offset = 0xFF }
    else { offset = (firstVisibleItemScrollOffset / 3) }

    return (offset.toLong() * 0X01000000)
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