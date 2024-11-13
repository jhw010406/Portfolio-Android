package com.example.tradingapp.view.home

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tradingapp.R
import com.example.tradingapp.model.data.navigation.MainNavigationGraph
import com.example.tradingapp.model.data.post.PostCategories
import com.example.tradingapp.model.viewmodel.post.getPostsList
import com.example.tradingapp.model.data.post.PostDetails
import com.example.tradingapp.model.viewmodel.home.HomeViewModel
import com.example.tradingapp.model.viewmodel.post.PreviewPostForTrading

@Composable
fun HomeView(
    tag : String,
    homeViewModel : HomeViewModel,
    getDeactivatedScreen : Boolean,
    setDeactivatedScreen : (Boolean) -> Unit,
    selectedPostDetails : (PostDetails) -> Unit,
    navigateFromMain : (route : String) -> Unit
){
    val deactivatedScreenColor : Color by animateColorAsState(
        targetValue =
        if (getDeactivatedScreen){
            Color(0xC0000000)
        }
        else{
            Color(0x00000000)
        }
    )

    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF212123))
    ){
        Column (
            modifier = Modifier
                .fillMaxSize()
        ) {
            // header
            HomeViewHeader()
            if (homeViewModel.lazyListState.firstVisibleItemIndex == 0){
                HorizontalDivider(
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 0.dp),
                    color = Color(0xFF636365)
                )
            }

            // body
            HomePostsList(tag, homeViewModel){ getSelectedPostDetails ->
                selectedPostDetails(getSelectedPostDetails)
            }
        }

        // the home view will be deactivated when button pressed
        if (getDeactivatedScreen){
            Surface (
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        setDeactivatedScreen(false)
                    },
                color = deactivatedScreenColor
            ) {}
        }

        // Write post button
        Box (
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = -12.dp, y = -16.dp)
        ) {
            WritePostForTradingButton(
                getDeactivatedScreen,
                setDeactivatedScreen
            ) { route ->
                navigateFromMain(route)
            }
        }
    }
}

@Composable
fun SelectThePurposeOfThePost(
    setDeactivatedScreen : (Boolean) -> Unit,
    navigateFromMain : (route : String) -> Unit
){
    Surface (
        modifier = Modifier
            .wrapContentSize(),
        shape = RoundedCornerShape(15),
        color = Color(0xFF212123)
    ) {
        Column (
            modifier = Modifier
                .padding(20.dp)
        ) {
            Row (
                modifier = Modifier
                    .clickable {
                        setDeactivatedScreen(false)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_multiple_sell_24),
                    modifier = Modifier
                        .size(24.dp),
                    tint = Color(0xFF8362E7),
                    contentDescription = "sell"
                )
                Spacer(modifier = Modifier.size(12.dp))

                Text(
                    text = "여러 물건 팔기",
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.size(16.dp))

            Row (
                modifier = Modifier
                    .clickable {
                        setDeactivatedScreen(false)
                        navigateFromMain(MainNavigationGraph.WRITEPOSTFORTRADING.name) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_sell_24),
                    modifier = Modifier
                        .size(24.dp),
                    tint = Color(0xFFE98341),
                    contentDescription = "sell"
                )
                Spacer(modifier = Modifier.size(12.dp))

                Text(
                    text = "내 물건 팔기",
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun WritePostForTradingButton(
    getDeactivatedScreen : Boolean,
    setDeactivatedScreen : (Boolean) -> Unit,
    navigateFromMain : (route : String) -> Unit
){
    val buttonPressed = rememberSaveable {
        mutableStateOf(getDeactivatedScreen)
    }
    val animateDp : Dp by animateDpAsState(
        targetValue =
        if (buttonPressed.value){ 8.dp }
        else{ 0.dp }
    )
    val animateColor : Color by animateColorAsState(
        targetValue =
        if (buttonPressed.value){ Color(0xFF212123) }
        else{ Color(0xFFFF6E1D) }
    )
    val animateRotation : Float by animateFloatAsState(
        targetValue =
        if (buttonPressed.value){ 45f }
        else{ 0f }
    )

    // recompose when the home view is activated
    buttonPressed.value = getDeactivatedScreen

    // Button for writing a post
    Column (
        modifier = Modifier
            .wrapContentSize(),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom
    ) {
        if (getDeactivatedScreen){
            SelectThePurposeOfThePost(
                setDeactivatedScreen = setDeactivatedScreen
            ) { route ->
                navigateFromMain(route)
            }
            Spacer(modifier = Modifier.size(8.dp))
        }

        Surface (
            modifier = Modifier
                .size(
                    width = 96.dp - animateDp * 6,
                    height = 48.dp
                )
                .clickable {
                    buttonPressed.value = !buttonPressed.value

                    if (buttonPressed.value) {
                        setDeactivatedScreen(true)
                    } else {
                        setDeactivatedScreen(false)
                    }
                },
            shape = CircleShape,
            color = animateColor
        ) {
            Row (
                modifier = Modifier
                    .fillMaxSize(),
                //horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_add_24),
                    modifier = Modifier
                        .size(32.dp)
                        .offset(x = 8.dp)
                        .graphicsLayer {
                            this.rotationZ = animateRotation
                        },
                    tint = Color.White,
                    contentDescription = "Write post button"
                )

                Text(
                    text = "글쓰기",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        )
                    ),
                    maxLines = 1,
                    modifier = Modifier
                        .offset(x = 8.dp + animateDp)
                )
            }
        }
    }
}

@Composable
fun HomeViewHeader(){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.CenterVertically)
            .background(color = Color(0xFF212123))
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
            Text(text = "location",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.size(4.dp))
            Icon(
                painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                tint = Color.White,
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
                tint = Color.White,
                contentDescription = "search product",
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.size(16.dp))

            // check notification
            Icon(
                painter = painterResource(id = R.drawable.baseline_notifications_none_24),
                tint = Color.White,
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
            modifier = Modifier
                .wrapContentSize(),
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFF111113)
        ){
            Row (
                modifier = Modifier
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    painter = painterResource(id = R.drawable.baseline_menu_24),
                    tint = Color.White,
                    contentDescription = "menu",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Spacer(modifier = Modifier.size(8.dp))

        // part-time job
        Surface(
            modifier = Modifier
                .wrapContentSize(),
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFF111113)
        ){
            Row (
                modifier = Modifier
                    .padding(start = 12.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    painter = painterResource(id = R.drawable.outline_person_search_24),
                    tint = Color.White,
                    contentDescription = "part-time job",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = "알바",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                )
            }
        }
        Spacer(modifier = Modifier.size(8.dp))

        // car
        Surface(
            modifier = Modifier
                .wrapContentSize(),
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFF111113)
        ){
            Row (
                modifier = Modifier
                    .padding(start = 12.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    painter = painterResource(id = R.drawable.baseline_directions_car_24),
                    tint = Color.White,
                    contentDescription = "second handed car",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = "중고차",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
        Spacer(modifier = Modifier.size(8.dp))

        // house
        Surface(
            modifier = Modifier
                .wrapContentSize(),
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFF111113)
        ){
            Row (
                modifier = Modifier
                    .padding(start = 12.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    painter = painterResource(id = R.drawable.baseline_other_houses_24),
                    tint = Color.White,
                    contentDescription = "house",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = "부동산",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                )
            }
        }
    }
}

@Composable
fun HomePostsList(
    tag : String,
    homeViewModel: HomeViewModel,
    selectedPostDetails : (PostDetails) -> Unit
){
    val pageCount = 10
    var postsList by rememberSaveable {
        mutableStateOf(homeViewModel.postsList.toList())
    }
    val lazyListState : LazyListState = homeViewModel.lazyListState
    val needMoreLoadPosts by remember(homeViewModel.startLoadingPostNumber) {
        derivedStateOf {
            if (homeViewModel.postsList.isNotEmpty()){
                lazyListState.firstVisibleItemIndex >= homeViewModel.startLoadingPostNumber
            }
            else{
                true
            }
        }
    }

    Log.d(tag, "needMore ${needMoreLoadPosts}")
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

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
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
                HorizontalDivider(
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 12.dp),
                    color = Color(0xFF636365)
                )
            }

            PreviewPostForTrading(tag = tag, previewPost = post){ getSelectedPostDetails ->
                selectedPostDetails(getSelectedPostDetails)
            }
        }
    }
}