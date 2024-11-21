package com.example.tradingapp.view.post.trading

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.tradingapp.R
import com.example.tradingapp.model.data.post.PostDetails
import com.example.tradingapp.model.data.user.UserInformation
import com.example.tradingapp.viewmodel.other.addDelimiterToPrice
import com.example.tradingapp.viewmodel.post.getTimeLagFromNow
import com.example.tradingapp.view.other.PostOptionsPanel
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PreviewTradingPost(
    tag : String,
    myInfo : UserInformation,
    previewPost : PostDetails,
    selectedPost : (selectedPostId : Int) -> Unit
){
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { selectedPost(previewPost.postID) }
            .padding(12.dp)
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
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
                modifier = Modifier.weight(2f)
            ) {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    // Title
                    Text(text = previewPost.title,
                        maxLines = 1,
                        fontSize = 24.sp,
                        modifier = Modifier.weight(9f),
                        overflow = TextOverflow.Ellipsis
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_more_vert_24),
                        tint = Color(0xFF8E8D91),
                        contentDescription = "more button",
                        modifier = Modifier.clickable {
                            PostOptionsPanel.show(myInfo.uid, previewPost.posterUID!!, previewPost.postID)
                        }
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