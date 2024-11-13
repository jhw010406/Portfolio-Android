package com.example.tradingapp.view.post.trading

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.tradingapp.R
import com.example.tradingapp.model.data.navigation.MainNavigationGraph
import com.example.tradingapp.model.data.post.PostCategories
import com.example.tradingapp.model.viewmodel.post.getPostsList
import com.example.tradingapp.model.data.post.PostDetails
import com.example.tradingapp.model.data.user.UserInformation
import com.example.tradingapp.model.viewmodel.verify.UserInformationViewModel
import com.example.tradingapp.model.viewmodel.post.MyPostsListViewModel
import com.example.tradingapp.model.viewmodel.post.PreviewPostForTrading

@Composable
fun UserPostsListView(
    tag : String,
    userId : String?,
    userUid : Int?,
    myPostsListViewModel : MyPostsListViewModel = viewModel(),
    mainNavController : NavHostController,
    selectedPost : (PostDetails) -> Unit
) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF212123))
    ) {
        MyPostsListHeader()

        MyPostsListProfile(userId, mainNavController)

        Column (
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row ( horizontalArrangement = Arrangement.Center) {
                Text(text = "판매중", fontWeight = FontWeight.SemiBold, color = Color.White)
            }

            MyPostsList(tag, userUid, myPostsListViewModel){ getPostDetails ->
                selectedPost(getPostDetails)
            }
        }

    }
}

@Composable
fun MyPostsListHeader(
    barColor : Color = Color(0xFF212123),
    dividerColor : Color = Color(0xFF636365)
){
    Column {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(align = Alignment.CenterVertically),
            color = barColor
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                    modifier = Modifier
                        .clickable {
                            //navController.popBackStack()
                        },
                    tint = Color.White,
                    contentDescription = "back",
                )
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
fun MyPostsListProfile(
    userId: String?,
    mainNavController: NavHostController
) {
    val targetID = userId ?: "나"

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = "${targetID}의 판매내역", fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
            Spacer(modifier = Modifier.size(12.dp))

            if (userId == null){
                Surface (
                    modifier = Modifier
                        .clickable { mainNavController.navigate(MainNavigationGraph.WRITEPOSTFORTRADING.name) },
                    shape = RoundedCornerShape(15),
                    color = Color(0x30FF6E1D)
                ) {
                    Text(
                        text = "글쓰기",
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFFF6E1D),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Surface (
            modifier = Modifier
                .size(60.dp),
            RoundedCornerShape(100)
        ) {

        }
    }
}

@Composable
fun MyPostsList(
    tag : String,
    userUid : Int?,
    myPostsListViewModel : MyPostsListViewModel,
    selectedPost : (PostDetails) -> Unit
){
    val pageCount = 10
    var postsList by rememberSaveable {
        mutableStateOf(myPostsListViewModel.tradingPostsList.toList())
    }
    val needMoreLoadPosts by remember(myPostsListViewModel.startLoadingPostNumber) {
        derivedStateOf {
            if (myPostsListViewModel.tradingPostsList.isNotEmpty()){
                myPostsListViewModel.lazyListState.value.firstVisibleItemIndex >= myPostsListViewModel.startLoadingPostNumber
            }
            else{
                true
            }
        }
    }

    LaunchedEffect (needMoreLoadPosts){
        getPostsList(
            tag,
            PostCategories.TRADING.value,
            myPostsListViewModel.startPageNumber,
            pageCount,
            userUid,
            myPostsListViewModel.tradingPostsList,
            false
        ) { isSuccessful ->

            if (isSuccessful){
                postsList = myPostsListViewModel.tradingPostsList.toList()
                myPostsListViewModel.startPageNumber = myPostsListViewModel.tradingPostsList.size
                myPostsListViewModel.startLoadingPostNumber += myPostsListViewModel.tradingPostsList.size / 2
            }
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = myPostsListViewModel.lazyListState.value
    ) {
        itemsIndexed(
            items = postsList,
            key = { index, post -> post.postID }
        ){  index, post ->

            if (index > 0){
                HorizontalDivider(
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 12.dp),
                    color = Color(0xFF636365)
                )
            }

            PreviewPostForTrading(tag = tag, previewPost = post) { getSelectedPostDetails ->
                selectedPost(getSelectedPostDetails)
            }
        }
    }
}