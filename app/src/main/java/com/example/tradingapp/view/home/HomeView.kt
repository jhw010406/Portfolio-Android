package com.example.tradingapp.view.home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tradingapp.R
import com.example.tradingapp.model.data.navigation.HomeNavigationGraph
import com.example.tradingapp.model.data.navigation.MainNavigationGraph
import com.example.tradingapp.model.data.post.PostCategories
import com.example.tradingapp.model.data.user.UserInformation
import com.example.tradingapp.viewmodel.clicklistener.MainNavGraphClickListener
import com.example.tradingapp.viewmodel.home.HomeViewModel
import com.example.tradingapp.viewmodel.post.getPostsList
import com.example.tradingapp.view.other.LoadingBar
import com.example.tradingapp.view.graph.HomeScreen
import com.example.tradingapp.view.post.trading.PreviewTradingPost

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeView(
    tag : String,
    myInfo : UserInformation,
    homeViewModel : HomeViewModel,
    backStackEntryId : String,
    homeNavController : NavHostController,
    mainNavController : NavHostController
){
    val isDeactivatedScreen = HomeScreen.deactiveHomeScreen.collectAsState().value
    val deactivatedScreenAlpha by animateFloatAsState(
        targetValue = if (isDeactivatedScreen){ 0.5f } else{ 0.0f }
    )

    Box (modifier = Modifier.fillMaxSize()){
        Column (
            modifier = Modifier
                .fillMaxSize()
                .drawWithContent {
                    drawContent()
                    drawRect(
                        color = Color.Black,
                        alpha = deactivatedScreenAlpha
                    )
                }
        ) {
            Log.d("recompose", "in 91")
            // header
            HomeViewHeader()
            if (homeViewModel.lazyListState.firstVisibleItemIndex == 0){
                HorizontalDivider(thickness = 1.dp)
            }

            // body
            HomePostsList(tag, myInfo, backStackEntryId, homeViewModel, mainNavController)
        }

        // create new click box when home screen is deactivated
        if (isDeactivatedScreen) {
            Box(modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = null,
                    indication = null
                ) { HomeScreen.active() }
            ){}
        }

        // Write post button
        WriteTradingPostButton(
            Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-12).dp, y = (-16).dp),
            isDeactivatedScreen,
            homeNavController,
            mainNavController
        )
    }
}

@Composable
fun TradingTypesList(
    mainNavController: NavHostController
){
    Surface (
        modifier = Modifier.wrapContentSize(),
        shape = RoundedCornerShape(15)
    ) {
        Column (
            modifier = Modifier.padding(20.dp)
        ) {
            Row (
                modifier = Modifier.clickable { HomeScreen.active() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_multiple_sell_24),
                    modifier = Modifier.size(24.dp),
                    tint = Color(0xFF8362E7),
                    contentDescription = "sell"
                )
                Spacer(Modifier.size(12.dp))

                Text(text = "여러 물건 팔기", fontWeight = FontWeight.SemiBold)
            }
            Spacer(Modifier.size(16.dp))

            Row (
                modifier = Modifier.clickable {
                        HomeScreen.active()
                        mainNavController.navigate(MainNavigationGraph.WRITEPOSTFORTRADING.name)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_sell_24),
                    modifier = Modifier.size(24.dp),
                    tint = Color(0xFFE98341),
                    contentDescription = "sell"
                )
                Spacer(Modifier.size(12.dp))

                Text(text = "내 물건 팔기", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun WriteTradingPostButton(
    modifier: Modifier,
    buttonPressed : Boolean,
    homeNavController : NavHostController,
    mainNavController : NavHostController
){
    var currentButtonSizeX = 0f
    val animateButtonSizeX by animateFloatAsState(
        targetValue = if (buttonPressed){ 2f } else{ 1f }
    )
    val animateAlpha by animateFloatAsState(
        targetValue = if (buttonPressed){ 0f } else{ 1f }
    )
    val animateButtonIconColor : Color by animateColorAsState(
        targetValue = if (buttonPressed){ MaterialTheme.colorScheme.onSurface } else{ Color.White }
    )
    val animateButtonSurfaceColor : Color by animateColorAsState(
        targetValue = if (buttonPressed){ MaterialTheme.colorScheme.surface } else{ Color(0xFFFF6E1D) }
    )
    val animateIconRotationZ : Float by animateFloatAsState(
        targetValue = if (buttonPressed){ 45f } else{ 0f }
    )

    // Button for writing a post
    Column (
        modifier = modifier.wrapContentSize(),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom
    ) {
        if (buttonPressed){
            TradingTypesList(mainNavController)
            Spacer(Modifier.size(8.dp))
        }

        Row (
            modifier = Modifier
                .size(width = 96.dp, height = 48.dp)
                .drawBehind {
                    drawRoundRect(
                        topLeft = Offset(
                            x = this.size.width - (this.size.width / animateButtonSizeX),
                            y = 0f
                        ),
                        color = animateButtonSurfaceColor,
                        cornerRadius = CornerRadius(100f, 100f),
                        size = Size(
                            width = this.size.width / animateButtonSizeX,
                            height = this.size.height
                        )
                    )
                }
                .clickable(
                    interactionSource = null,
                    indication = null,
                    onClick = {
                        homeNavController.currentBackStackEntry?.destination?.route.let {

                            if (it.equals(HomeNavigationGraph.HOME.name)) {

                                if (buttonPressed) {
                                    HomeScreen.active()
                                } else {
                                    HomeScreen.deactive()
                                }
                            }
                        }
                    }
                )
                .onSizeChanged { newSize -> currentButtonSizeX = newSize.width.toFloat() },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_add_24),
                modifier = Modifier
                    .size(32.dp)
                    .offset {
                        IntOffset(
                            x = (currentButtonSizeX - (currentButtonSizeX / animateButtonSizeX)).toInt() - 8,
                            y = 0
                        )
                    }
                    .graphicsLayer { this.rotationZ = animateIconRotationZ },
                tint = animateButtonIconColor,
                contentDescription = "Write post button"
            )

            Text(
                text = "글쓰기",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                style = TextStyle(platformStyle = PlatformTextStyle(false)),
                maxLines = 1,
                modifier = Modifier
                    .graphicsLayer(alpha = animateAlpha)
                    .offset {
                        IntOffset(
                            x = (currentButtonSizeX - (currentButtonSizeX / animateButtonSizeX)).toInt() - 8,
                            y = 0
                        )
                    }
            )
        }
    }
}

@Composable
fun HomeViewHeader(){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.CenterVertically)
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 12.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Row (
            modifier = Modifier.clickable(
                interactionSource = null,
                indication = null
            ) {},
            horizontalArrangement = Arrangement.Start
        ){
            Text(text = "location",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.size(4.dp))

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
            Icon(
                painter = painterResource(id = R.drawable.baseline_search_24),
                contentDescription = "search product",
                modifier = Modifier.size(28.dp)
            )
            Spacer(Modifier.size(16.dp))

            // check notification
            Icon(
                painter = painterResource(id = R.drawable.baseline_notifications_none_24),
                contentDescription = "check notification",
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
fun UpperCategory(){
    // category
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.CenterVertically)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        // menu
        Surface(
            modifier = Modifier.wrapContentSize(),
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.secondary
        ){
            Row (
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    painter = painterResource(id = R.drawable.baseline_menu_24),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = "menu",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Spacer(Modifier.size(8.dp))

        // part-time job
        Surface(
            modifier = Modifier.wrapContentSize(),
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.secondary
        ){
            Row (
                modifier = Modifier.padding(start = 12.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    painter = painterResource(id = R.drawable.outline_person_search_24),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = "part-time job",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.size(4.dp))

                Text(
                    text = "알바",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
        Spacer(Modifier.size(8.dp))

        // car
        Surface(
            modifier = Modifier.wrapContentSize(),
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.secondary
        ){
            Row (
                modifier = Modifier.padding(start = 12.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    painter = painterResource(id = R.drawable.baseline_directions_car_24),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = "second handed car",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.size(4.dp))

                Text(
                    text = "중고차",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
        Spacer(Modifier.size(8.dp))

        // house
        Surface(
            modifier = Modifier.wrapContentSize(),
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.secondary
        ){
            Row (
                modifier = Modifier.padding(start = 12.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    painter = painterResource(id = R.drawable.baseline_other_houses_24),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = "house",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.size(4.dp))

                Text(
                    text = "부동산",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomePostsList(
    tag : String,
    myInfo : UserInformation,
    backStackEntryId : String,
    homeViewModel: HomeViewModel,
    mainNavController: NavHostController
){
    var postsList by rememberSaveable { mutableStateOf(homeViewModel.postsList.toList()) }
    val lazyListState : LazyListState = homeViewModel.lazyListState
    val needMoreLoadPosts by remember(homeViewModel.startLoadingPostNumber) {
        derivedStateOf {
            if (homeViewModel.postsList.isNotEmpty()){
                lazyListState.firstVisibleItemIndex >= homeViewModel.startLoadingPostNumber
            }
            else{ true }
        }
    }

    val pageCount = 10
    LaunchedEffect(needMoreLoadPosts) {
        getPostsList(
            tag,
            PostCategories.TRADING.value,
            homeViewModel.startPageNumber,
            pageCount,
            null,
            homeViewModel.postsList,
            false
        ) { isSuccessful ->

            if (isSuccessful){
                postsList = homeViewModel.postsList.toList()
                homeViewModel.startPageNumber = homeViewModel.postsList.size
                homeViewModel.startLoadingPostNumber += homeViewModel.postsList.size / 2
            }
        }
    }

    val reloadPostsList = homeViewModel.reloadPostsList.value
    LaunchedEffect(reloadPostsList) {

        if (reloadPostsList) {
            postsList = homeViewModel.postsList.toList()
            homeViewModel.reloadPostsList.value = false
        }
    }

    if (reloadPostsList) {
        LoadingBar.show()
    }
    else {
        LoadingBar.hide()
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
            state = lazyListState
        ) {
            // This item's index is 0
            item ( key = "category" ) {
                UpperCategory()
            }

            // more than 0 index from this item
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
}