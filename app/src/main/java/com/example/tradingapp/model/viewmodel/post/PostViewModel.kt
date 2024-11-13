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
import coil.compose.AsyncImage
import com.example.tradingapp.R
import com.example.tradingapp.model.data.post.PostCategories
import com.example.tradingapp.model.data.post.PostDetails
import com.example.tradingapp.model.repository.PostDataRepository
import com.example.tradingapp.model.viewmodel.other.addDelimiterToPrice
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

    if (firstVisibleItemIndex != 0){
        return (0xFF000000)
    }

    if ((firstVisibleItemScrollOffset / 3) >= 0xFF){
        offset = 0xFF
    }
    else {
        offset = (firstVisibleItemScrollOffset / 3)
    }

    return (offset.toLong() * 0X01000000)
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


@Composable
fun PreviewPostForTrading(
    tag : String,
    previewPost : PostDetails,
    selectedPostDetails : (PostDetails) -> Unit
){
    val currentContext = LocalContext.current
    var isSelected by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(isSelected) {
        if (isSelected) {
            getSelectedPostDetails(tag, previewPost.postID) { getPostDetails, isSuccessful ->
                if (isSuccessful) {

                    if (getPostDetails != null) {
                        selectedPostDetails(getPostDetails)
                    } else {
                        Toast
                            .makeText(currentContext, "존재하지 않는 게시글입니다.", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast
                        .makeText(currentContext, "서버 에러", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            isSelected = false
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isSelected = true }
            .padding(12.dp),
        color = Color(0xFF212123)
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image
            if (previewPost.thumbnailImageUrl != null){
                AsyncImage(
                    model = previewPost.thumbnailImageUrl,
                    modifier = Modifier
                        .weight(1f)
                        .height(120.dp)
                        .clip(RoundedCornerShape(8)),
                    contentScale = ContentScale.Crop,
                    contentDescription = "product thumbnail"
                )
            }
            Spacer(modifier = Modifier.size(12.dp))

            // Content
            Column (
                modifier = Modifier
                    .weight(2f)
            ) {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    // Title
                    Text(text = previewPost.title,
                        maxLines = 1,
                        fontSize = 24.sp,
                        color = Color.White,
                        modifier = Modifier.weight(9f),
                        overflow = TextOverflow.Ellipsis
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_more_vert_24),
                        tint = Color(0xFF8E8D91),
                        contentDescription = "more button",
                        modifier = Modifier
                            .clickable {  }
                    )
                }
                Spacer(modifier = Modifier.size(4.dp))

                // location, posted time
                Row (
                    modifier = Modifier.fillMaxWidth()
                ){
                    Text(
                        text = "주소 • ${getTimeLagFromNow(LocalDateTime.parse(previewPost.uploadDate))} 전",
                        maxLines = 1,
                        color = Color(0xFF8E8D91),
                        overflow = TextOverflow.Ellipsis)
                }
                Spacer(modifier = Modifier.size(4.dp))

                // price
                Text(text = "${addDelimiterToPrice(previewPost.postDetailsTrading!!.productPrice.toString())}원",
                    maxLines = 1,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.size(4.dp))

                // others
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    // chatting
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_chat_bubble_outline_24),
                            tint = Color(0xFF8E8D91),
                            modifier = Modifier.size(16.dp),
                            contentDescription = "chatting"
                        )
                        Text(
                            text = " ${previewPost.postDetailsTrading.chatCount}",
                            color = Color(0xFF8E8D91),
                            fontSize = 12.sp,
                            maxLines = 1
                        )
                    }
                    Spacer(modifier = Modifier.size(12.dp))

                    // favorites
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_favorite_border_24),
                            tint = Color(0xFF8E8D91),
                            modifier = Modifier.size(16.dp),
                            contentDescription = "favorites"
                        )
                        Text(
                            text = " ${previewPost.postDetailsTrading.favoriteCount}",
                            color = Color(0xFF8E8D91),
                            fontSize = 12.sp,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}