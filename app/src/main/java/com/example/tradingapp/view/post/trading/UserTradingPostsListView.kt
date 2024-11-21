package com.example.tradingapp.view.post.trading

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.material3.MaterialTheme
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
import com.example.tradingapp.model.data.user.UserInformation
import com.example.tradingapp.viewmodel.clicklistener.MainNavGraphClickListener
import com.example.tradingapp.viewmodel.post.UserPostsListViewModel
import com.example.tradingapp.viewmodel.post.getPostsList
import com.example.tradingapp.view.other.LoadingView

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UserPostsListView(
    tag : String,
    isMyPostsList : Boolean,
    myInfo : UserInformation,
    userId : String?,
    userUid : Int?,
    userPostsListViewModel : UserPostsListViewModel = viewModel(),
    mainNavController : NavHostController
) {
    val backStackEntryId = userPostsListViewModel.backStackEntryId.value

    if (backStackEntryId == null) {
        LoadingView()
        userPostsListViewModel.backStackEntryId.value = mainNavController.currentBackStackEntry?.id
    }
    else {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            UserPostsListHeader(mainNavController = mainNavController)

            UserPostsListProfile(isMyPostsList, userId, mainNavController)

            Column (
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row ( horizontalArrangement = Arrangement.Center) {
                    Text(text = "판매중", fontWeight = FontWeight.SemiBold)
                }

                UserPostsList(
                    tag,
                    backStackEntryId,
                    myInfo,
                    userUid,
                    userPostsListViewModel,
                    mainNavController
                )
            }
        }
    }
}

@Composable
fun UserPostsListHeader(
    mainNavController : NavHostController
){
    Column {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(align = Alignment.CenterVertically)
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
                    modifier = Modifier.clickable { mainNavController.popBackStack() },
                    contentDescription = "back",
                )
            }
        }
        HorizontalDivider(thickness = 1.dp)
    }
}

@Composable
fun UserPostsListProfile(
    isMyPostsList : Boolean,
    userId: String?,
    mainNavController: NavHostController
) {
    val targetID = if (isMyPostsList) { "나" } else { userId }

    Row (
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = "${targetID}의 판매내역", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.size(12.dp))

            if (isMyPostsList){
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
            modifier = Modifier.size(60.dp),
            RoundedCornerShape(100)
        ) {

        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UserPostsList(
    tag : String,
    backStackEntryId : String,
    myInfo: UserInformation,
    userUid : Int?,
    userPostsListViewModel : UserPostsListViewModel,
    mainNavController : NavHostController
){
    val pageCount = 10
    var postsList by rememberSaveable { mutableStateOf(userPostsListViewModel.tradingPostsList.toList()) }
    val needMoreLoadPosts by remember(userPostsListViewModel.startLoadingPostNumber) {
        derivedStateOf {
            if (userPostsListViewModel.tradingPostsList.isNotEmpty()){
                userPostsListViewModel.lazyListState.value.firstVisibleItemIndex >= userPostsListViewModel.startLoadingPostNumber
            }
            else{ true }
        }
    }


    LaunchedEffect (needMoreLoadPosts){
        getPostsList(
            tag,
            PostCategories.TRADING.value,
            userPostsListViewModel.startPageNumber,
            pageCount,
            userUid,
            userPostsListViewModel.tradingPostsList,
            false
        ) { isSuccessful ->

            if (isSuccessful){
                postsList = userPostsListViewModel.tradingPostsList.toList()
                userPostsListViewModel.startPageNumber = userPostsListViewModel.tradingPostsList.size
                userPostsListViewModel.startLoadingPostNumber += userPostsListViewModel.tradingPostsList.size / 2
            }
        }
    }


    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = userPostsListViewModel.lazyListState.value
    ) {
        itemsIndexed(
            items = postsList,
            key = { index, post -> post.postID }
        ){  index, post ->

            if (index > 0){
                HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(horizontal = 12.dp))
            }

            PreviewTradingPost(
                tag = tag,
                myInfo = myInfo,
                previewPost = post
            ) { selectedPostId ->

                mainNavController.currentBackStackEntry?.savedStateHandle?.set("post_id", selectedPostId)
                MainNavGraphClickListener.navigate(
                    backStackEntryId,
                    MainNavigationGraph.TRADINGPOST.name,
                    mainNavController
                )
            }
        }
    }
}