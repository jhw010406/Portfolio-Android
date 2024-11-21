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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tradingapp.R
import com.example.tradingapp.model.data.navigation.MainNavigationGraph
import com.example.tradingapp.model.data.user.UserInformation

@Composable
fun MyProfileView(
    tag : String,
    myInformation : UserInformation,
    mainNavController : NavHostController
){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        MyProfileHeader(mainNavController = mainNavController)

        // my profile
        MyProfileInfo(myInformation, mainNavController)
        Spacer(modifier = Modifier.size(16.dp))

        // my posts
        MyTradingPosts(myInformation, mainNavController)
        HorizontalDivider(thickness = 1.dp)
        Spacer(modifier = Modifier.size(16.dp))

        MyCommunityPosts()
    }
}

@Composable
fun MyProfileHeader(
    mainNavController: NavHostController
){
    Column{
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(align = Alignment.CenterVertically)
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
                        contentDescription = "more functions for the post",
                        modifier = Modifier
                            .size(28.dp)
                            .clickable {
                                mainNavController.navigate(MainNavigationGraph.SETTINGOPTIONS.name)
                            }
                    )
                }
            }
        }
        HorizontalDivider(thickness = 1.dp)
    }
}

@Composable
fun MyProfileInfo(
    myInformation: UserInformation,
    mainNavController: NavHostController
){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row (verticalAlignment = Alignment.CenterVertically) {
            Surface (
                modifier = Modifier.size(36.dp),
                shape = RoundedCornerShape(100)
            ) {

            }
            Spacer(modifier = Modifier.size(16.dp))

            Text(text = myInformation.nickname!!, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
        }

        Surface (
            modifier = Modifier.clickable {},
            shape = RoundedCornerShape(10),
            color = MaterialTheme.colorScheme.secondary
        ) {
            Text(text = "프로필 수정", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(horizontal = 12.dp), color = MaterialTheme.colorScheme.onSurface)
        }
    }
}

@Composable
fun MyTradingPosts(
    myInformation: UserInformation,
    mainNavController: NavHostController
) {
    Column {
        Text(text = "나의 거래", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(modifier = Modifier.size(8.dp))

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .clickable { mainNavController.navigate(MainNavigationGraph.FAVORITEPOSTSLIST.name) }
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(painter = painterResource(id = R.drawable.outline_favorite_border_24), contentDescription = "favorite list")
            Text(text = " 관심목록")
        }

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    mainNavController.currentBackStackEntry?.savedStateHandle?.set("user_uid", myInformation.uid)
                    mainNavController.navigate(MainNavigationGraph.USERTRADINGPOSTSLIST.name)
                }
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(painter = painterResource(id = R.drawable.outline_receipt_long_24), contentDescription = "sell list")
            Text(text = " 판매내역")
        }

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                }
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(painter = painterResource(id = R.drawable.outline_shopping_bag_24), contentDescription = "buy list")
            Text(text = " 구매내역")
        }
        Spacer(modifier = Modifier.size(12.dp))
    }
}

@Composable
fun MyCommunityPosts() {
    Column {
        Text(text = "나의 동네생활", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(modifier = Modifier.size(8.dp))

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .clickable {}
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(painter = painterResource(id = R.drawable.outline_edit_note_24), contentDescription = "my community posts")
            Text(text = " 내 동네생활 글")
        }
    }
}