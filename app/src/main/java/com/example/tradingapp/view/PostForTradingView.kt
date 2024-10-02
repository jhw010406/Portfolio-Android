package com.example.tradingapp.view

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tradingapp.R

@Preview
@Composable
fun PostForTradingView(){
    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
    ){
        item (
            key = "header"
        ){
            PostForTradingHeader()
        }
        item(
            key = "post"
        ){
            PostForTradingContent()
        }
        item (
            key = "comments"
        ){

        }
        item (
            key = "trending post"
        ){

        }
    }
}

@Composable
fun PostForTradingHeader(){
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.CenterVertically),
        color = Color(0xFF333335)
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Row (
                modifier = Modifier
                    .clickable {

                    },
                horizontalArrangement = Arrangement.Start
            ){
                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                    contentDescription = "back",
                )
            }

            Row (
                horizontalArrangement = Arrangement.Center
            ){
                // turn on alarm or turn off
                Icon(painter = painterResource(id = R.drawable.outline_notifications_off_24),
                    contentDescription = "set alarm",
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.size(16.dp))

                // share the post
                Icon(painter = painterResource(id = R.drawable.outline_share_24),
                    contentDescription = "share the post",
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.size(16.dp))

                // more
                Icon(painter = painterResource(id = R.drawable.baseline_more_vert_24),
                    contentDescription = "more functions for the post",
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
}

@Composable
fun PostForTradingContent(){
    
}