package com.example.tradingapp.view

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.tradingapp.R
import com.example.tradingapp.model.data.PreviewItemData
import com.example.tradingapp.model.viewmodel.HomeViewModel

@Composable
fun HomeView(
    navController : NavHostController,
    getDeactivatedScreen : Boolean,
    setDeactivatedScreen : (Boolean) -> Unit
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
        modifier = Modifier.fillMaxSize()
    ){
        Column (
            modifier = Modifier
                .fillMaxSize()
        ) {
            // header
            HomeViewHeader()
            HorizontalDivider(
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 0.dp),
                color = Color(0xFF636365)
            )

            // body
            HomeItemList()
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
            ) {
            }
        }

        // Write post button
        Box (
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = -12.dp, y = -16.dp)
        ) {
            WritePostForTradingButton(
                navController,
                getDeactivatedScreen,
                setDeactivatedScreen
            )
        }
    }
}

@Composable
fun SelectThePurposeOfThePost(
    navController : NavHostController,
    setDeactivatedScreen : (Boolean) -> Unit
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
                        navController.navigate("WritePostForTrading")
                        setDeactivatedScreen(false)
                    },
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
    navController : NavHostController,
    getDeactivatedScreen : Boolean,
    setDeactivatedScreen : (Boolean) -> Unit
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
                navController = navController,
                setDeactivatedScreen = setDeactivatedScreen
            )
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
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Row (
                modifier = Modifier
                    .clickable {

                    },
                horizontalArrangement = Arrangement.Start
            ){
                Text(text = "test",
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
fun HomeItemList(){
    val homeViewModel : HomeViewModel = viewModel()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        itemsIndexed(
            items = homeViewModel.GetItemList(),
            key = { index, item ->
                item.id
            }
        ){  index, item ->
            if (index == 0){
                UpperCategory()
            }
            PreviewItemView(item = item)
            HorizontalDivider(
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 12.dp),
                color = Color(0xFF636365)
            )
        }
    }
}

@Composable
fun PreviewItemView(item : PreviewItemData){
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(12.dp),
        color = Color(0xFF212123)
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image
            Text(text = item.productImage,
                modifier = Modifier
                    .weight(1f),
                textAlign = TextAlign.Center
            )

            // Content
            Column (
                modifier = Modifier
                    .weight(2f)
            ) {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    // Title
                    Text(text = item.title,
                        maxLines = 2,
                        fontSize = 24.sp,
                        color = Color.White,
                        modifier = Modifier.weight(9f),
                        overflow = TextOverflow.Ellipsis
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_more_vert_24),
                        tint = Color(0xFF8E8D91),
                        contentDescription = "more button",
                        modifier = Modifier
                            .clickable {  }
                    )
                }
                Spacer(modifier = Modifier.size(4.dp))

                // location, posted time
                Row (
                    modifier = Modifier.fillMaxWidth()
                ){
                    Text(
                        text = "${item.address.townShip} • ${item.postingTime}",
                        maxLines = 1,
                        color = Color(0xFF8E8D91),
                        overflow = TextOverflow.Ellipsis)
                }
                Spacer(modifier = Modifier.size(4.dp))

                // price
                Text(text = "${item.price}원",
                    maxLines = 1,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.size(4.dp))

                // others
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    // chatting
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_chat_bubble_outline_24),
                            tint = Color(0xFF8E8D91),
                            modifier = Modifier.size(16.dp),
                            contentDescription = "chatting"
                        )
                        Text(
                            text = " ${item.chattingCount}",
                            color = Color(0xFF8E8D91),
                            fontSize = 12.sp,
                            maxLines = 1
                        )
                    }
                    Spacer(modifier = Modifier.size(12.dp))

                    // favorites
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_favorite_border_24),
                            tint = Color(0xFF8E8D91),
                            modifier = Modifier.size(16.dp),
                            contentDescription = "favorites"
                        )
                        Text(
                            text = " ${item.favoritesCount}",
                            color = Color(0xFF8E8D91),
                            fontSize = 12.sp,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}