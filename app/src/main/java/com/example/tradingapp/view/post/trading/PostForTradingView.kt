package com.example.tradingapp.view.post.trading

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.tradingapp.R
import com.example.tradingapp.model.data.navigation.MainNavigationGraph
import com.example.tradingapp.model.data.post.PostCategories
import com.example.tradingapp.model.data.post.PostDetails
import com.example.tradingapp.model.viewmodel.other.addDelimiterToPrice
import com.example.tradingapp.model.viewmodel.post.PostForTradingViewModel
import com.example.tradingapp.model.viewmodel.post.getAlphaValueForHeader
import com.example.tradingapp.model.viewmodel.post.getPostsList
import com.example.tradingapp.model.viewmodel.post.getSelectedPostDetails
import com.example.tradingapp.model.viewmodel.post.getTimeLagFromNow
import java.time.LocalDateTime

@Composable
fun PostForTradingView(
    tag : String,
    myUid: Int,
    postDetails: PostDetails,
    mainNavController: NavHostController,
    getUserInfo : (getUserUid : Int?, getUserId : String?) -> Unit,
    selectedPostDetails : (PostDetails) -> Unit,
    postForTradingViewModel: PostForTradingViewModel = viewModel()
){
    val bodyLazyListState = rememberLazyListState()

    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF212123))
    ) {
        Column {
            PostForTradingBody(
                tag,
                Modifier.weight(1f),
                bodyLazyListState,
                postDetails,
                mainNavController,
                selectedPostDetails = { getSelectedPostDetails ->
                    selectedPostDetails(getSelectedPostDetails)
                },
                getUserInfo = { getUserUid, getUserId ->
                    getUserInfo(getUserUid, getUserId)
                }
            )

            // footer
            PostForTradingFooter(
                tag = tag,
                myUid = myUid,
                postDetails = postDetails,
                postForTradingViewModel = postForTradingViewModel
            )
        }

        PostForTradingHeader(bodyLazyListState = bodyLazyListState, mainNavController = mainNavController)
    }
}

@Composable
fun PostForTradingHeader(
    bodyLazyListState : LazyListState,
    mainNavController: NavController
){
    val barColor : Color by animateColorAsState(targetValue =
        Color(getAlphaValueForHeader(
            bodyLazyListState.firstVisibleItemIndex,
            bodyLazyListState.firstVisibleItemScrollOffset ) + 0x212123
        )
    )
    val dividerColor : Color by animateColorAsState(targetValue =
        Color(getAlphaValueForHeader(
            bodyLazyListState.firstVisibleItemIndex,
            bodyLazyListState.firstVisibleItemScrollOffset ) + 0x636365
        )
    )

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
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                    modifier = Modifier.clickable { mainNavController.popBackStack() },
                    tint = Color.White,
                    contentDescription = "back",
                )

                Row {
                    // share the post
                    Icon(painter = painterResource(id = R.drawable.outline_share_24),
                        tint = Color.White,
                        contentDescription = "share the post",
                        modifier = Modifier
                            .size(28.dp)
                            .clickable {}
                    )
                    Spacer(modifier = Modifier.size(16.dp))

                    // more
                    Icon(painter = painterResource(id = R.drawable.baseline_more_vert_24),
                        tint = Color.White,
                        contentDescription = "more functions for the post",
                        modifier = Modifier
                            .size(28.dp)
                            .clickable {}
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
fun PostForTradingFooter(
    tag : String,
    myUid : Int,
    postDetails: PostDetails,
    postForTradingViewModel: PostForTradingViewModel
){
    val currentContext = LocalContext.current
    var favoriteIconIdx by rememberSaveable { mutableStateOf(0) }
    val favoriteIcon by remember {
        mutableStateOf(
            listOf(
                R.drawable.outline_favorite_border_24,
                R.drawable.baseline_favorite_24
            )
        )
    }
    var selectedFavoriteButton by rememberSaveable { mutableStateOf<Boolean?>(null) }
    var isFavorited by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect (selectedFavoriteButton) {

        if (selectedFavoriteButton == null) {
            postForTradingViewModel.isFavoritedPost(
                tag,
                myUid,
                postDetails.postID,
            ) { getIsFavorited ->
                isFavorited = getIsFavorited

                if (isFavorited) favoriteIconIdx = 1
                else    favoriteIconIdx = 0

                Log.d(tag, "isFavorited ${isFavorited}")
            }
        }
        else {
            if (selectedFavoriteButton!!) {

                if (isFavorited) {
                    postForTradingViewModel.unfavoritePost(tag, myUid, postDetails.postID) { isSuccessful ->

                        if (isSuccessful) {
                            isFavorited = false
                            favoriteIconIdx = 0
                        }
                        else {
                            Toast.makeText(currentContext, "찜 취소 실패. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                        }

                        selectedFavoriteButton = false
                    }
                }
                else {
                    postForTradingViewModel.favoritePost(tag, myUid, postDetails.postID) { isSuccessful ->

                        if (isSuccessful) {
                            isFavorited = true
                            favoriteIconIdx = 1
                        }
                        else {
                            Toast.makeText(currentContext, "찜하기 실패. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                        }

                        selectedFavoriteButton = false
                    }
                }
            }
        }
    }

    HorizontalDivider(
        thickness = 1.dp,
        color = Color(0xFF636365)
    )
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .padding(horizontal = 16.dp)
            .background(color = Color(0xFF212123)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Row (
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = favoriteIcon[favoriteIconIdx]),
                modifier = Modifier
                    .size(20.dp)
                    .clickable {
                        if ((selectedFavoriteButton == null) || (!(selectedFavoriteButton!!))) {
                            selectedFavoriteButton = true
                        }
                    },
                tint = Color.White,
                contentDescription = "favorite button"
            )
            Spacer(modifier = Modifier.size(16.dp))

            VerticalDivider(
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 10.dp),
                color = Color(0xFF636365)
            )
            Spacer(modifier = Modifier.size(16.dp))

            Column {
                Text(text = "${addDelimiterToPrice(postDetails.postDetailsTrading!!.productPrice.toString())}원", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                Text(
                    text = "가격 제안하기",
                    modifier = Modifier
                        .clickable {},
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    textDecoration = TextDecoration.Underline,
                    color = Color(0xFFFF6E1D)
                )
            }
        }

        Surface(
            modifier = Modifier
                .size(width = 80.dp, height = 36.dp)
                .clickable {

                },
            shape = RoundedCornerShape(15),
            color = Color(0xFFFF6E1D)
        ) {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "채팅하기", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
            }
        }
    }
}

@Composable
fun PostForTradingBody(
    tag : String,
    modifier : Modifier,
    bodyLazyListState : LazyListState,
    postDetails: PostDetails,
    mainNavController : NavHostController,
    selectedPostDetails : (PostDetails) -> Unit,
    getUserInfo : (getUserUid : Int?, getUserId : String?) -> Unit,
){
    var needLoadOtherTradingPostsList by rememberSaveable { mutableStateOf(true) }
    val getOtherTradingPostsList = rememberSaveable { mutableListOf<PostDetails>() }
    var otherTradingPostsList by rememberSaveable { mutableStateOf< List<PostDetails> >(listOf()) }
    var otherTradingPostsListSize by rememberSaveable { mutableIntStateOf(0) }
    val pagerState = rememberPagerState(
        pageCount = { postDetails.postDetailsTrading!!.productImagesForGet!!.size }
    )

    if (needLoadOtherTradingPostsList) {
        // get other trading posts list
        LaunchedEffect(Unit) {

            getPostsList(
                tag,
                PostCategories.TRADING.value,
                0,
                4,
                postDetails.posterUID,
                getOtherTradingPostsList,
                false
            ) { isSuccessful ->

                if (isSuccessful){
                    otherTradingPostsList = getOtherTradingPostsList.toList()
                    otherTradingPostsListSize = getOtherTradingPostsList.size
                }

                needLoadOtherTradingPostsList = false
            }
        }
    }

    LazyColumn (
        modifier = modifier,
        state = bodyLazyListState
    ) {
        // product images
        item {
            HorizontalPager(
                modifier = Modifier.wrapContentSize(),
                state = pagerState
            ) { pageNumber ->
                AsyncImage(
                    model = postDetails.postDetailsTrading!!.productImagesForGet!![pageNumber].first,
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .height(400.dp),
                    contentScale = ContentScale.Crop,
                    contentDescription = "product image"
                )
            }
        }

        // user's profile
        item {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .clickable { getUserInfo(postDetails.posterUID, postDetails.posterID) },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // user's profile image
                    Surface(
                        modifier = Modifier
                            .size(36.dp),
                        shape = RoundedCornerShape(100)
                    ) {

                    }
                    Spacer(modifier = Modifier.size(12.dp))


                    // user's ID and address
                    Column {
                        Text(
                            text = "${postDetails.posterID}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                        Text(text = "주소", fontSize = 12.sp, color = Color.White)
                    }
                }
                HorizontalDivider(
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 12.dp),
                    color = Color(0xFF636365)
                )

                PostForTradingContent(postDetails)
                Spacer(modifier = Modifier.size(16.dp))
                HorizontalDivider(
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 12.dp),
                    color = Color(0xFF636365)
                )
            }
        }

        // user's other products
        item {
            Column (
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { getUserInfo(postDetails.posterUID, postDetails.posterID) },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${postDetails.posterID}님의 판매 물품",
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                        modifier = Modifier.size(16.dp),
                        tint = Color.White,
                        contentDescription = "other products"
                    )
                }

                Column ( modifier = Modifier.padding(vertical = 16.dp) ) {
                    if (otherTradingPostsListSize > 0){
                        Row ( modifier = Modifier.fillMaxWidth() ) {
                            PreviewOtherProduct(
                                tag = tag,
                                postDetails = otherTradingPostsList[0],
                                modifier = Modifier.weight(1f)
                            ) { getSelectedPostDetails ->
                                selectedPostDetails(getSelectedPostDetails)
                            }
                            Spacer(modifier = Modifier.size(16.dp))

                            if (otherTradingPostsListSize > 1){
                                PreviewOtherProduct(
                                    tag = tag,
                                    postDetails = otherTradingPostsList[1],
                                    modifier = Modifier.weight(1f)
                                ) { getSelectedPostDetails ->
                                    selectedPostDetails(getSelectedPostDetails)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.size(24.dp))
                    }

                    if (otherTradingPostsListSize > 2){
                        Row ( modifier = Modifier.fillMaxWidth() ) {
                            PreviewOtherProduct(
                                tag = tag,
                                postDetails = otherTradingPostsList[2],
                                modifier = Modifier.weight(1f)
                            ) { getSelectedPostDetails ->
                                selectedPostDetails(getSelectedPostDetails)
                            }
                            Spacer(modifier = Modifier.size(16.dp))

                            if (otherTradingPostsListSize > 3){
                                PreviewOtherProduct(
                                    tag = tag,
                                    postDetails = otherTradingPostsList[3],
                                    modifier = Modifier.weight(1f)
                                ) { getSelectedPostDetails ->
                                    selectedPostDetails(getSelectedPostDetails)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PostForTradingContent(
    postDetails: PostDetails
){
    Column (
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ){
        Spacer(modifier = Modifier.size(16.dp))

        Text(text = postDetails.title, fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
        Spacer(modifier = Modifier.size(4.dp))

        Text(
            text = "${postDetails.postDetailsTrading!!.productType} • ${getTimeLagFromNow(LocalDateTime.parse(postDetails.uploadDate))} 전",
            fontSize = 12.sp,
            color = Color(0xFF969698)
        )
        Spacer(modifier = Modifier.size(16.dp))

        Text(text = postDetails.content!!, fontSize = 16.sp, color = Color.White)
        Spacer(modifier = Modifier.size(16.dp))

        Text(text = "채팅 ${postDetails.postDetailsTrading.chatCount} • 관심 ${postDetails.postDetailsTrading!!.favoriteCount} • 조회 ${postDetails.viewCount}", fontSize = 12.sp, color = Color(0xFF969698))
    }
}

@Composable
fun PreviewOtherProduct(
    tag : String,
    postDetails: PostDetails,
    modifier: Modifier,
    selectedPostDetails : (PostDetails) -> Unit
){
    val currentContext = LocalContext.current
    var isSelected by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(isSelected) {

        if (isSelected){
            getSelectedPostDetails(tag, postDetails.postID){ getPostDetails, isSuccessful ->
                if (isSuccessful){

                    if (getPostDetails != null){
                        selectedPostDetails(getPostDetails)
                    }
                    else{
                        Toast.makeText(currentContext, "존재하지 않는 게시글입니다.", Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                    Toast.makeText(currentContext, "서버 에러", Toast.LENGTH_SHORT).show()
                }
            }

            isSelected = false
        }
    }

    Column (
        modifier = modifier.clickable { isSelected = true }
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .height(160.dp)
                .clip(RoundedCornerShape(8)),
            model = postDetails.thumbnailImageUrl,
            contentScale = ContentScale.Crop,
            contentDescription = "product image"
        )
        Spacer(modifier = Modifier.size(8.dp))

        Text(text = postDetails.title, color = Color.White)

        Text(text = "${addDelimiterToPrice(postDetails.postDetailsTrading!!.productPrice.toString())}원", fontWeight = FontWeight.SemiBold, color = Color.White)
    }
}