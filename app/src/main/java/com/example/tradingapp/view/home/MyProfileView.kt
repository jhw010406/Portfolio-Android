package com.example.tradingapp.view.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tradingapp.R
import com.example.tradingapp.model.data.navigation.MainNavigationGraph
import com.example.tradingapp.model.data.user.UserInformation

@Composable
fun MyProfileView(
    tag : String,
    myInformation : UserInformation,
    navigateFromMain : (route : String) -> Unit
){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF212123))
    ) {
        MyProfileHeader { route ->
            navigateFromMain(route)
        }

        // my profile
        MyProfileInfo(myInformation) { route ->
            navigateFromMain(route)
        }
        Spacer(modifier = Modifier.size(16.dp))

        // my posts
        MyTradingPosts{ route -> navigateFromMain(route) }
        HorizontalDivider(
            thickness = 1.dp,
            modifier = Modifier.padding(horizontal = 0.dp),
            color = Color(0xFF636365)
        )
        Spacer(modifier = Modifier.size(16.dp))

        MyCommunityPosts()
    }
}

@Composable
fun MyProfileHeader(
    barColor : Color = Color(0xFF212123),
    dividerColor : Color = Color(0xFF636365),
    navigateFromMain: (route: String) -> Unit
){
    Column () {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(align = Alignment.CenterVertically),
            color = barColor
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ){
                Row {
                    // setting
                    Icon(painter = painterResource(id = R.drawable.outline_settings_24),
                        tint = Color.White,
                        contentDescription = "more functions for the post",
                        modifier = Modifier
                            .size(28.dp)
                            .clickable {
                                navigateFromMain(MainNavigationGraph.SETTINGOPTIONS.name)
                            }
                    )
                }
            }
        }
        HorizontalDivider(
            thickness = 1.dp,
            modifier = Modifier.padding(horizontal = 0.dp),
            color = dividerColor
        )
    }
}

@Composable
fun MyProfileInfo(
    myInformation: UserInformation,
    navigateFromMain : (route : String) -> Unit
){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface (
                modifier = Modifier
                    .size(36.dp),
                shape = RoundedCornerShape(100)
            ) {

            }
            Spacer(modifier = Modifier.size(16.dp))

            Text(text = myInformation.nickname!!, fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
        }

        Surface (
            modifier = Modifier
                .clickable {
                },
            shape = RoundedCornerShape(10),
            color = Color(0xFF424249)
        ) {
            Text(text = "프로필 수정", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(horizontal = 12.dp), color = Color.White)
        }
    }
}

@Composable
fun MyTradingPosts(
    navigateFromMain : (route : String) -> Unit
) {
    Column (
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ) {
        Text(text = "나의 거래", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
        Spacer(modifier = Modifier.size(8.dp))

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .clickable { navigateFromMain(MainNavigationGraph.FAVORITEPOSTSLIST.name) }
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(painter = painterResource(id = R.drawable.outline_favorite_border_24), tint = Color.White, contentDescription = "favorite list")
            Text(text = " 관심목록", color = Color.White)
        }

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    navigateFromMain(MainNavigationGraph.USERTRADINGPOSTSLIST.name)
                }
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(painter = painterResource(id = R.drawable.outline_receipt_long_24), tint = Color.White, contentDescription = "sell list")
            Text(text = " 판매내역", color = Color.White)
        }

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .clickable {

                }
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(painter = painterResource(id = R.drawable.outline_shopping_bag_24), tint = Color.White, contentDescription = "buy list")
            Text(text = " 구매내역", color = Color.White)
        }
        Spacer(modifier = Modifier.size(12.dp))
    }
}

@Composable
fun MyCommunityPosts() {
    Column (
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ) {
        Text(text = "나의 동네생활", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
        Spacer(modifier = Modifier.size(8.dp))

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    
                }
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(painter = painterResource(id = R.drawable.outline_edit_note_24), tint = Color.White, contentDescription = "my community posts")
            Text(text = " 내 동네생활 글", color = Color.White)
        }
    }
}