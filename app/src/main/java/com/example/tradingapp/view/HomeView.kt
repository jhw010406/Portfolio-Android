package com.example.tradingapp.view

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tradingapp.R
import com.example.tradingapp.model.data.Address
import com.example.tradingapp.model.data.PreviewItemData
import com.example.tradingapp.model.viewmodel.HomeViewModel

@Preview
@Composable
fun HomeScreen(

){
    Surface (
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF333335)
    ){
        Column (
            modifier = Modifier
                .fillMaxSize()
        ) {
            // header
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(align = Alignment.CenterVertically)
                    .padding(vertical = 12.dp),
                color = Color(0xFF333335)
            ) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Row (
                        modifier = Modifier
                            .clickable {

                            },
                        horizontalArrangement = Arrangement.Start
                    ){
                        Text(text = "test",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.size(4.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                            contentDescription = "select living location",
                            modifier = Modifier
                                .rotate(-90f)
                                .offset(x = 4.dp)
                        )
                    }

                    Row (
                        horizontalArrangement = Arrangement.Center
                    ){
                        // search product
                        Icon(painter = painterResource(id = R.drawable.baseline_search_24),
                            contentDescription = "search product",
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.size(16.dp))

                        // check notification
                        Icon(painter = painterResource(id = R.drawable.baseline_notifications_none_24),
                            contentDescription = "check notification",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
            HorizontalDivider(
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 0.dp),
                color = Color(0xFF636365)
            )

            // body
            Surface(
                color = Color(0xFF333335)
            ) {
                HomeItemList()
            }
        }
    }
}

@Composable
fun HomeItemList(){
    val homeViewModel : HomeViewModel = viewModel()
    var paddingValue : Float

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        itemsIndexed(
            items = homeViewModel.GetItemList(),
            key = { index, item ->
                item.id
            }
        ){  index, item ->
            PreviewItemView(item = item)
            Log.d("test", "${item.title}")
            HorizontalDivider(
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 12.dp),
                color = Color(0xFF636365)
            )
        }
    }
}

@Composable
fun PreviewItemView(item : PreviewItemData){
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(12.dp),
        color = Color(0xFF333335)
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image
            Text(text = "${item.productImage}",
                modifier = Modifier
                    .weight(1f),
                textAlign = TextAlign.Center
            )

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
                    Text(text = "${item.title}",
                        maxLines = 2,
                        fontSize = 24.sp,
                        color = Color.White,
                        modifier = Modifier.weight(9f),
                        overflow = TextOverflow.Ellipsis
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_more_vert_24),
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
                    Text(text = "${item.address.townShip} • ${item.postingTime}",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis)
                }
                Spacer(modifier = Modifier.size(4.dp))

                // price
                Text(text = "${item.price}원",
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
                        Icon(painter = painterResource(id = R.drawable.baseline_chat_bubble_outline_24),
                            modifier = Modifier.size(16.dp),
                            contentDescription = "chatting")
                        Text(text = " ${item.chattingCount}",
                            fontSize = 12.sp,
                            maxLines = 1)
                    }
                    Spacer(modifier = Modifier.size(12.dp))

                    // favorites
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(painter = painterResource(id = R.drawable.baseline_favorite_border_24),
                            modifier = Modifier.size(16.dp),
                            contentDescription = "favorites")
                        Text(text = " ${item.favoritesCount}",
                            fontSize = 12.sp,
                            maxLines = 1)
                    }
                }
            }
        }
    }
}