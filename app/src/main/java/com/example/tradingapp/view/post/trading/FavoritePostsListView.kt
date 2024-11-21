package com.example.tradingapp.view.post.trading

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.tradingapp.R
import com.example.tradingapp.model.data.navigation.MainNavigationGraph
import com.example.tradingapp.model.data.post.PostCategories
import com.example.tradingapp.model.data.post.PostDetails
import com.example.tradingapp.model.data.user.UserInformation
import com.example.tradingapp.model.viewmodel.clicklistener.MainNavGraphClickListener
import com.example.tradingapp.model.viewmodel.post.FavoritePostsListViewModel
import com.example.tradingapp.model.viewmodel.post.getPostsList
import com.example.tradingapp.view.other.LoadingView

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FavoritePostsListView(
    tag : String,
    myInfo : UserInformation,
    mainNavController: NavHostController,
    favoritePostsListViewModel: FavoritePostsListViewModel = viewModel()
) {
    val backStackEntryId = favoritePostsListViewModel.backStackEntryId.value

    if (backStackEntryId == null) {
        LoadingView()
        favoritePostsListViewModel.backStackEntryId.value = mainNavController.currentBackStackEntry?.id
    }
    else {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            FavoritePostsListViewHeader(mainNavController = mainNavController)

            FavoritePostsListViewBody(
                tag = tag,
                modifier = Modifier.weight(1f),
                backStackEntryId = backStackEntryId,
                mainNavController = mainNavController,
                myInfo = myInfo
            )
        }
    }
}

@Composable
fun FavoritePostsListViewHeader(
    mainNavController: NavController
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
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                    modifier = Modifier.clickable { mainNavController.popBackStack() },
                    contentDescription = "back",
                )
                Text(text = "관심목록", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
        }
        HorizontalDivider(thickness = 1.dp)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FavoritePostsListViewBody(
    tag : String,
    modifier: Modifier,
    backStackEntryId : String,
    myInfo: UserInformation,
    mainNavController: NavHostController
) {
    val pageCount = 10
    val getFavoritePostsList : MutableList<PostDetails> = mutableListOf()
    val lazyListState : LazyListState = rememberLazyListState()
    var favoritePostsList by rememberSaveable { mutableStateOf<List<PostDetails>>(listOf()) }
    val needLoadMorePosts by rememberSaveable { mutableStateOf(true) }


    LaunchedEffect (needLoadMorePosts) {

        if (needLoadMorePosts) {
            getPostsList(
                tag,
                PostCategories.TRADING.value,
                favoritePostsList.size,
                pageCount,
                myInfo.uid,
                getFavoritePostsList,
                true
            ) { isSuccessful ->

                if (isSuccessful) {
                    favoritePostsList = getFavoritePostsList.toList()
                    Log.d(tag, "list size : ${favoritePostsList.size}")
                }
            }
        }
    }


    LazyColumn (
        modifier = modifier.fillMaxSize(),
        state = lazyListState
    ) {
        itemsIndexed(
            items = favoritePostsList,
            key = { index, post -> index }
        ) { index, post ->
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