package com.example.tradingapp.view.post.trading

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.tradingapp.R
import com.example.tradingapp.model.data.navigation.MainNavigationGraph
import com.example.tradingapp.model.data.post.PostCategories
import com.example.tradingapp.model.data.post.PostDetails
import com.example.tradingapp.model.data.user.UserInformation
import com.example.tradingapp.viewmodel.clicklistener.MainNavGraphClickListener
import com.example.tradingapp.viewmodel.home.HomeViewModel
import com.example.tradingapp.viewmodel.other.addDelimiterToPrice
import com.example.tradingapp.viewmodel.post.TradingPostViewModel
import com.example.tradingapp.viewmodel.post.getAlphaValueForHeader
import com.example.tradingapp.viewmodel.post.getPostsList
import com.example.tradingapp.viewmodel.post.getSelectedPostDetails
import com.example.tradingapp.viewmodel.post.getTimeLagFromNow
import com.example.tradingapp.view.other.LoadingView
import com.example.tradingapp.view.other.RootSnackbar
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TradingPostView(
    tag : String,
    myInfo : UserInformation,
    postId : Int,
    homeViewModel: HomeViewModel,
    mainNavController: NavHostController,
    tradingPostViewModel: TradingPostViewModel
){
    val currentContext = LocalContext.current
    val backStackEntryId = tradingPostViewModel.backStackEntryId.value
    // 화면 회전으로 인한 recompose시에는 post reload 불가
    var loadedPost by rememberSaveable { mutableStateOf(false) }
    val bodyLazyListState = rememberLazyListState()
    val postDetails = tradingPostViewModel.postDetails

    LaunchedEffect(Unit) {

        if (backStackEntryId == null) {
            tradingPostViewModel.backStackEntryId.value = mainNavController.currentBackStackEntry?.id
        }

        if (tradingPostViewModel.postDetails == null) {

            getSelectedPostDetails(tag, postId) { getPostDetails, isSuccessful ->

                if (isSuccessful) {

                    if (getPostDetails != null) {
                        tradingPostViewModel.postDetails = getPostDetails
                        loadedPost = true
                    }
                    else {
                        RootSnackbar.show("게시글 접근에 실패했습니다. 다시 시도해주세요.")
                        mainNavController.currentBackStackEntry?.destination?.route.let {

                            if (it.equals(MainNavigationGraph.TRADINGPOST.name)){
                                mainNavController.popBackStack()
                            }
                        }
                    }
                }
                else {
                    RootSnackbar.show("존재하지 않는 게시글입니다.")
                    homeViewModel.removeItemByPostId(postId)
                    homeViewModel.reloadPostsList.value = true
                    mainNavController.currentBackStackEntry?.destination?.route.let {

                        if (it.equals(MainNavigationGraph.TRADINGPOST.name)){
                            mainNavController.popBackStack()
                        }
                    }
                }
            }
        }
    }

    if (!loadedPost || backStackEntryId == null) {
        LoadingView()
    }
    else {
        Box (
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column {
                TradingPostBody(
                    tag,
                    Modifier.weight(1f),
                    backStackEntryId,
                    bodyLazyListState,
                    postDetails!!,
                    mainNavController
                )

                // footer
                TradingPostFooter(
                    tag = tag,
                    myUid = myInfo.uid,
                    postDetails = postDetails,
                    tradingPostViewModel = tradingPostViewModel
                )
            }

            TradingPostHeader(bodyLazyListState = bodyLazyListState, mainNavController = mainNavController)
        }
    }
}

@Composable
fun TradingPostHeader(
    bodyLazyListState : LazyListState,
    mainNavController: NavController
){
    val surfaceColor = MaterialTheme.colorScheme.surface
    val firstVisibleItemIndex by remember {
        derivedStateOf { bodyLazyListState.firstVisibleItemIndex }
    }
    val firstVisibleItemScrollOffset by remember {
        derivedStateOf { bodyLazyListState.firstVisibleItemScrollOffset }
    }
    val animateSurfaceAlpha by animateFloatAsState(targetValue =
        getAlphaValueForHeader(firstVisibleItemIndex, firstVisibleItemScrollOffset)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .drawBehind {
                drawRect(
                    color = surfaceColor,
                    alpha = animateSurfaceAlpha
                )
            }
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Box{
                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                    contentDescription = "back",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { mainNavController.popBackStack() }
                )
                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                    contentDescription = "back",
                    tint = Color.White,
                    modifier = Modifier
                        .size(28.dp)
                        .graphicsLayer { alpha = (1f - animateSurfaceAlpha) }
                )
            }

            Row {
                // share the post
                Box {
                    Icon(painter = painterResource(id = R.drawable.outline_share_24),
                        contentDescription = "share the post",
                        modifier = Modifier
                            .size(28.dp)
                            .clickable {}
                    )
                    Icon(painter = painterResource(id = R.drawable.outline_share_24),
                        contentDescription = "share the post",
                        tint = Color.White,
                        modifier = Modifier
                            .size(28.dp)
                            .graphicsLayer { alpha = (1f - animateSurfaceAlpha) }
                    )
                }
                Spacer(modifier = Modifier.size(16.dp))

                // more
                Box{
                    Icon(painter = painterResource(id = R.drawable.baseline_more_vert_24),
                        contentDescription = "more functions for the post",
                        modifier = Modifier
                            .size(28.dp)
                            .clickable {}
                    )
                    Icon(painter = painterResource(id = R.drawable.baseline_more_vert_24),
                        contentDescription = "more functions for the post",
                        tint = Color.White,
                        modifier = Modifier
                            .size(28.dp)
                            .graphicsLayer { alpha = (1f - animateSurfaceAlpha) }
                    )
                }
            }
        }
        HorizontalDivider(thickness = 1.dp, modifier = Modifier.graphicsLayer { alpha = animateSurfaceAlpha })
    }
}

@Composable
fun TradingPostFooter(
    tag : String,
    myUid : Int,
    postDetails: PostDetails,
    tradingPostViewModel: TradingPostViewModel
){
    val currentContext = LocalContext.current
    var favoriteIconIdx by rememberSaveable { mutableStateOf(0) }
    val favoriteIcon = listOf(
        R.drawable.outline_favorite_border_24,
        R.drawable.baseline_favorite_24
    )
    var pressedFavoriteButton by rememberSaveable { mutableStateOf<Boolean?>(null) }
    var isFavorited by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect (pressedFavoriteButton) {

        if (pressedFavoriteButton == null) {
            tradingPostViewModel.isFavoritedPost(tag, myUid, postDetails.postID) { getIsFavorited ->
                isFavorited = getIsFavorited

                if (isFavorited) favoriteIconIdx = 1
                else    favoriteIconIdx = 0

                Log.d(tag, "isFavorited ${isFavorited}")
            }
        }
        else {
            if (pressedFavoriteButton!!) {

                if (isFavorited) {
                    tradingPostViewModel.unfavoritePost(tag, myUid, postDetails.postID) { isSuccessful ->

                        if (isSuccessful) {
                            isFavorited = false
                            favoriteIconIdx = 0
                        }
                        else {
                            RootSnackbar.show("찜 취소 실패. 다시 시도해주세요.")
                        }

                        pressedFavoriteButton = false
                    }
                }
                else {
                    tradingPostViewModel.favoritePost(tag, myUid, postDetails.postID) { isSuccessful ->

                        if (isSuccessful) {
                            isFavorited = true
                            favoriteIconIdx = 1
                        }
                        else {
                            RootSnackbar.show("찜하기 실패. 다시 시도해주세요.")
                        }

                        pressedFavoriteButton = false
                    }
                }
            }
        }
    }

    HorizontalDivider(thickness = 1.dp)
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .padding(horizontal = 16.dp)
            .background(MaterialTheme.colorScheme.background),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Row (
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = favoriteIcon[favoriteIconIdx]),
                tint = Color(0xFF636365),
                modifier = Modifier
                    .size(20.dp)
                    .clickable {
                        if ((pressedFavoriteButton == null) || (!(pressedFavoriteButton!!))) {
                            pressedFavoriteButton = true
                        }
                    },
                contentDescription = "favorite button"
            )
            Spacer(modifier = Modifier.size(16.dp))

            VerticalDivider(thickness = 1.dp, modifier = Modifier.padding(vertical = 10.dp))
            Spacer(modifier = Modifier.size(16.dp))

            Column {
                Text(
                    text = "${addDelimiterToPrice(postDetails.postDetailsTrading!!.productPrice.toString())}원",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "가격 제안하기",
                    modifier = Modifier.clickable {},
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TradingPostBody(
    tag : String,
    modifier : Modifier,
    backStackEntryId : String,
    bodyLazyListState : LazyListState,
    postDetails: PostDetails,
    mainNavController : NavHostController
){
    var pressedUserProfile by rememberSaveable { mutableStateOf(false) }
    var needLoadOtherTradingPostsList by rememberSaveable { mutableStateOf(true) }
    val getOtherTradingPostsList = rememberSaveable { mutableListOf<PostDetails>() }
    var otherTradingPostsList by rememberSaveable { mutableStateOf< List<PostDetails> >(listOf()) }
    var otherTradingPostsListSize by rememberSaveable { mutableIntStateOf(0) }
    val pagerState = rememberPagerState(
        pageCount = { postDetails.postDetailsTrading!!.productImagesForGet!!.size }
    )

    // get other trading posts list
    LaunchedEffect(needLoadOtherTradingPostsList) {

        if (needLoadOtherTradingPostsList) {
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
            }

            needLoadOtherTradingPostsList = false
        }
    }

    LaunchedEffect(pressedUserProfile) {

        if (pressedUserProfile) {
            mainNavController.currentBackStackEntry?.savedStateHandle?.set("user_id", postDetails.posterID)
            mainNavController.currentBackStackEntry?.savedStateHandle?.set("user_uid", postDetails.posterUID)
            MainNavGraphClickListener.navigate(
                backStackEntryId,
                MainNavigationGraph.USERTRADINGPOSTSLIST.name,
                mainNavController
            )
            pressedUserProfile = false
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
                    model = postDetails.postDetailsTrading!!.productImagesForGet!![pageNumber].url,
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
                        .clickable { pressedUserProfile = true }
                        .padding(horizontal = 16.dp, vertical = 12.dp),
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
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(text = "주소", fontSize = 12.sp)
                    }
                }
                HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(horizontal = 12.dp))

                TradingPostContent(postDetails)
                Spacer(modifier = Modifier.size(16.dp))
                HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(horizontal = 12.dp))
            }
        }

        // user's other products
        item {
            Column (modifier = Modifier.padding(8.dp, 16.dp)) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .clickable(
                            interactionSource = null,
                            indication = null
                        ) { pressedUserProfile = true },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "${postDetails.posterID}님의 판매 물품", fontWeight = FontWeight.SemiBold)
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                        modifier = Modifier.size(16.dp),
                        contentDescription = "other products"
                    )
                }

                // user's other trading posts list
                Column ( modifier = Modifier.padding(vertical = 16.dp) ) {
                    for (startIdx : Int in 0..1){
                        Row (modifier = Modifier.fillMaxWidth()) {
                            for (idx : Int in (startIdx * 2)..(startIdx * 2 + 1)){
                                if (idx < otherTradingPostsListSize) {
                                    PreviewOtherProduct(
                                        tag = tag,
                                        postDetails = otherTradingPostsList[idx],
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(horizontal = 8.dp)
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
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TradingPostContent(
    postDetails: PostDetails
){
    Column (
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ){
        Spacer(modifier = Modifier.size(16.dp))

        Text(text = postDetails.title, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.size(4.dp))

        Text(
            text = "${postDetails.postDetailsTrading!!.productType}" +
                    " • ${getTimeLagFromNow(LocalDateTime.parse(postDetails.uploadDate))} 전",
            fontSize = 12.sp,
            color = Color(0xFF969698)
        )
        Spacer(modifier = Modifier.size(16.dp))

        Text(text = postDetails.content!!, fontSize = 16.sp)
        Spacer(modifier = Modifier.size(16.dp))

        Text(text = "채팅 ${postDetails.postDetailsTrading.chatCount}" +
                " • 관심 ${postDetails.postDetailsTrading!!.favoriteCount}" +
                " • 조회 ${postDetails.viewCount}",
            fontSize = 12.sp,
            color = Color(0xFF969698)
        )
    }
}

@Composable
fun PreviewOtherProduct(
    tag : String,
    postDetails: PostDetails,
    modifier: Modifier,
    selectPostId : (Int) -> Unit
){
    Column (
        modifier = modifier.clickable(
                interactionSource = null,
                indication = null
        ) { selectPostId(postDetails.postID) }
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

        Text(text = postDetails.title)

        Text(
            text = "${addDelimiterToPrice(postDetails.postDetailsTrading!!.productPrice.toString())}원",
            fontWeight = FontWeight.SemiBold
        )
    }
}