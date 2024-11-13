package com.example.tradingapp.view.post.trading

import android.util.Log
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.tradingapp.R
import com.example.tradingapp.model.data.post.PostCategories
import com.example.tradingapp.model.data.post.PostDetails
import com.example.tradingapp.model.viewmodel.post.PreviewPostForTrading
import com.example.tradingapp.model.viewmodel.post.getPostsList

@Preview
@Composable
fun FavoritePostsListView(
    tag : String,
    myUid : Int,
    mainNavController: NavHostController,
    selectedPostDetails : (PostDetails) -> Unit
) {

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF212123))
    ) {
        FavoritePostsListViewHeader(mainNavController = mainNavController)

        FavoritePostsListViewBody(
            tag = tag,
            modifier = Modifier.weight(1f),
            myUid = myUid
        ) { getPostDetails ->
            selectedPostDetails(getPostDetails)
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
                .wrapContentHeight(align = Alignment.CenterVertically),
            color = Color(0xFF212123)
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
                    tint = Color.White,
                    contentDescription = "back",
                )
                Text(text = "관심목록", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
            }
        }
        HorizontalDivider(
            thickness = 1.dp,
            modifier = Modifier.padding(horizontal = 0.dp),
            color = Color(0xFF636365)
        )
    }
}

@Composable
fun FavoritePostsListViewBody(
    tag : String,
    modifier: Modifier,
    myUid : Int,
    selectedPostDetails : (PostDetails) -> Unit
) {
    val pageCount = 10
    val getFavoritePostsList : MutableList<PostDetails> = mutableListOf()
    val lazyListState : LazyListState = rememberLazyListState()
    var favoritePostsList by rememberSaveable { mutableStateOf<List<PostDetails>>(listOf()) }
    var needLoadMorePosts by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect (needLoadMorePosts) {

        if (needLoadMorePosts) {
            getPostsList(
                tag,
                PostCategories.TRADING.value,
                favoritePostsList.size,
                pageCount,
                myUid,
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
            PreviewPostForTrading(tag = tag, previewPost = post) { getPostDetails ->
                selectedPostDetails(getPostDetails)
            }
        }
    }
}