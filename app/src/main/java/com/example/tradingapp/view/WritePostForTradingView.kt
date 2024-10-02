package com.example.tradingapp.view

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import com.example.tradingapp.R
import com.example.tradingapp.model.data.OtherNavigationGraph
import com.example.tradingapp.model.viewmodel.WritePostForTradingViewModel

@Composable
fun WritePostForTradingScreen(
    navController : NavHostController
){
    val writePostForTradingViewModel : WritePostForTradingViewModel = viewModel()

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF212123))
    ){
        WritePostForTradingScreenHeader(navController)
        HorizontalDivider(
            thickness = 1.dp,
            modifier = Modifier.padding(horizontal = 0.dp),
            color = Color(0xFF636365)
        )

        Column (
        ){
            // body
            LazyColumn (
                modifier = Modifier
                    .weight(1f)
            ){
                item {
                    WritePostForTradingScreenBody(writePostForTradingViewModel)
                }
            }

            // footer
            Surface (
                modifier = Modifier
                    .padding(horizontal = 12.dp),
                color = Color(0xFF212123)
            ){
                WritePostForTradingScreenFooter()
            }
        }
    }
}

@Composable
fun WritePostForTradingScreenHeader(
    navController : NavHostController
){
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestinationView = navBackStackEntry?.destination?.route

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.CenterVertically),
        color = Color(0xFF212123)
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Row (
                modifier = Modifier
                    .clickable {
                        if (currentDestinationView.equals(OtherNavigationGraph.WRITEPOSTFORTRADING.name)){
                            navController.popBackStack()
                        }
                    },
                horizontalArrangement = Arrangement.Start
            ){
                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                    tint = Color.White,
                    contentDescription = "back",
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(text = "내 물건 팔기",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Row (
                horizontalArrangement = Arrangement.Center
            ){
                Row(
                    modifier = Modifier
                        .wrapContentWidth()
                        .height(28.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = "임시저장",
                        fontSize = 14.sp,
                        style = TextStyle(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            )
                        ),
                        textAlign = TextAlign.Center,
                        color = Color(0xFF636365)
                    )
                }
            }
        }
    }
}

@Composable
fun WritePostForTradingScreenBody(
    postInformations : WritePostForTradingViewModel
){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp)
    ){
        // upload images
        Spacer(modifier = Modifier.size(8.dp))
        UploadImagesForTrading(postInformations)
        Spacer(modifier = Modifier.size(32.dp))

        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ){
            // write title
            WritePostTitle(postInformations)
            Spacer(modifier = Modifier.size(32.dp))

            // write how to trade
            SelectHowToTrade(postInformations)
            Spacer(modifier = Modifier.size(32.dp))

            // write details
            WriteProductDetails(postInformations)
            Spacer(modifier = Modifier.size(32.dp))

            // write the location that trading will be done
            SelectTradingLocation(postInformations)
            Spacer(modifier = Modifier.size(32.dp))
        }
        Log.d("title", "${postInformations.postTitle}")
    }
}

@Composable
fun UploadImagesForTrading(
    postInformations : WritePostForTradingViewModel
){
    val getMultipleImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(10)
    ) { uris: List<Uri> ->
        uris.forEach { uri ->
            postInformations.selectedImages.add(uri)
        }
    }
    var imageCountTextColor : Color

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.Bottom
    ){
        // upload images button
        Spacer(modifier = Modifier.size(16.dp))
        Surface (
            modifier = Modifier
                .size(68.dp),
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFF212123),
            border = BorderStroke(
                width = 1.dp,
                color = Color(0xFF51545B)
            )
        ){
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        if (postInformations.selectedImages.size < 10) {
                            getMultipleImageLauncher.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        }
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Icon(
                    painter = painterResource(id = R.drawable.baseline_photo_camera_24),
                    tint = Color(0xFF858C94),
                    contentDescription = "upload images"
                )
                Row {
                    if (postInformations.selectedImages.size > 0){
                        imageCountTextColor = Color(0xFFFF6E1D)
                    }
                    else{
                        imageCountTextColor = Color(0xFF858C94)
                    }

                    Text(
                        text = "${postInformations.selectedImages.size}",
                        textAlign = TextAlign.Center,
                        color = imageCountTextColor
                    )
                    Text(
                        text = "/10",
                        textAlign = TextAlign.Center,
                        color = Color(0xFF858C94)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.size(12.dp))

        // The list of the images that is uploaded
        LazyRow (
            modifier = Modifier
                .fillMaxSize(),
        ){
            itemsIndexed(
                items = postInformations.selectedImages.toList(),
                key = { index: Int, uri: Uri? ->
                    index
                }
            ){  index: Int, uri: Uri? ->
                Spacer(modifier = Modifier.size(12.dp))
                Box (
                    modifier = Modifier
                        .size(68.dp),
                ){
                    // image
                    Surface (
                        modifier = Modifier
                            .fillMaxSize(),
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF212123)
                    ){
                        AsyncImage(
                            model = uri,
                            contentDescription = "product image"
                        )
                    }

                    // Remove image button
                    Surface (
                        modifier = Modifier
                            .size(20.dp)
                            .offset(x = 56.dp, y = -8.dp)
                            .clickable {
                                // remove this image
                                postInformations.selectedImages.remove(uri)
                            },
                        shape = CircleShape,
                        color = Color(0xFFEAEBEF)
                    ){
                        Icon(
                            painter = painterResource(id = R.drawable.outline_close_24),
                            tint = Color(0xFF212123),
                            contentDescription = "remove image"
                        )
                    }
                }
                Spacer(modifier = Modifier.size(12.dp))
            }
        }
    }
}

@Composable
fun WritePostTitle(
    postInformations : WritePostForTradingViewModel
){
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ){
        Text(
            text = "제목",
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.size(16.dp))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            maxLines = 1,
            placeholder = {
                Text(
                    text = "제목",
                    color = Color(0xFF858C94)
                )
            },
            value = postInformations.postTitle.value,
            onValueChange = { input ->
                if (!input.any(){
                    char -> char == '\n'
                }){
                    postInformations.postTitle.value = input
                }
            }
        )
    }
}

@Composable
fun SelectHowToTrade(
    postInformations : WritePostForTradingViewModel
){
    var toSellButtonColor : Color = Color(0xFFCFD3DC)
    var toSellButtonTextColor : Color = Color(0xFF212123)
    var toSellButtonBorder : BorderStroke? = null
    var toGiveButtonColor : Color = Color(0xFFCFD3DC)
    var toGiveButtonTextColor : Color = Color(0xFF212123)
    var toGiveButtonBorder : BorderStroke? = null

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ){
        Text(
            text = "거래 방식",
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.size(16.dp))

        Row (

        ){
            if (postInformations.forSelling.value){
                toSellButtonColor = Color(0xFFCFD3DC)
                toSellButtonTextColor = Color(0xFF212123)
                toSellButtonBorder = null
                toGiveButtonColor = Color(0xFF212123)
                toGiveButtonTextColor = Color.White
                toGiveButtonBorder = BorderStroke(
                    width = 1.dp,
                    color = Color(0xFF636365)
                )
            }
            else{
                toSellButtonColor = Color(0xFF212123)
                toSellButtonTextColor = Color.White
                toSellButtonBorder = BorderStroke(
                    width = 1.dp,
                    color = Color(0xFF636365)
                )
                toGiveButtonColor = Color(0xFFCFD3DC)
                toGiveButtonTextColor = Color(0xFF212123)
                toGiveButtonBorder = null
            }

            // to sell
            Button(
                onClick = {
                    postInformations.forSelling.value = true
                },
                colors = ButtonColors(
                    containerColor = toSellButtonColor,
                    contentColor = toSellButtonTextColor,
                    disabledContainerColor = Color(0xFF212123),
                    disabledContentColor = Color.White
                ),
                border = toSellButtonBorder
            ) {
                Text(
                    text = "판매하기",
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.size(8.dp))

            // to give
            Button(
                onClick = {
                    postInformations.forSelling.value = false
                },
                colors = ButtonColors(
                    containerColor = toGiveButtonColor,
                    contentColor = toGiveButtonTextColor,
                    disabledContainerColor = Color(0xFF212123),
                    disabledContentColor = Color.White
                ),
                border = toGiveButtonBorder
            ) {
                Text(
                    text = "나눔하기",
                    fontSize = 16.sp
                )
            }
        }
        Spacer(modifier = Modifier.size(16.dp))

        // Sell
        if (postInformations.forSelling.value){
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                maxLines = 1,
                placeholder = {
                    Text(
                        text = "가격을 입력해주세요.",
                        color = Color(0xFF858C94)
                    )
                },
                prefix = {
                    Text(
                        text = "₩ ",
                        color = Color(0xFF858C94)
                    )
                },
                value = postInformations.productPrice.value,
                onValueChange = { input ->
                    if (input.all { char -> char.isDigit() }){
                        postInformations.productPrice.value = input
                    }
                }
            )
            Spacer(modifier = Modifier.size(16.dp))

            // accept other price request
            Row (
                modifier = Modifier
                    .wrapContentSize()
                    .clickable {
                        postInformations.acceptOtherPriceRequest.value =
                            !postInformations.acceptOtherPriceRequest.value
                    },
                verticalAlignment = Alignment.CenterVertically
            ){
                Checkbox(
                    modifier = Modifier
                        .size(20.dp),
                    checked = postInformations.acceptOtherPriceRequest.value,
                    onCheckedChange = { input ->
                        postInformations.acceptOtherPriceRequest.value = input
                    }
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "가격 제안 받기",
                    color = Color.White,
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        )
                    )
                )
            }
        }
        // Give
        else{
            Row (
                modifier = Modifier
                    .wrapContentSize()
                    .clickable {
                        postInformations.acceptFreebieRequest.value =
                            !postInformations.acceptFreebieRequest.value
                    },
                verticalAlignment = Alignment.CenterVertically
            ){
                Checkbox(
                    modifier = Modifier
                        .size(20.dp),
                    checked = postInformations.acceptFreebieRequest.value,
                    onCheckedChange = { input ->
                        postInformations.acceptFreebieRequest.value = input
                    }
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "나눔 신청 받기",
                    color = Color.White,
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        )
                    )
                )
            }
        }
        Log.d("forSelling", "${postInformations.forSelling}")
    }
}

@Composable
fun WriteProductDetails(
    postInformations : WritePostForTradingViewModel
){
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ){
        Text(
            text = "자세한 설명",
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.size(16.dp))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = {
                Text(
                    text = "~에 올릴 게시글 내용을 작성해 주세요.\n" +
                            "(판매 금지 물품은 게시가 제한될 수 있어요.)\n\n" +
                            "신뢰할 수 있는 거래를 위해 자세히 적어주세요.\n" +
                            "과학기술정보통신부, 한국 인터넷진흥원과 함께 해요.",
                    color = Color(0xFF858C94)
                )
            },
            value = postInformations.productDetails.value,
            onValueChange = { input ->
                postInformations.productDetails.value = input
            }
        )
    }
}

@Composable
fun SelectTradingLocation(
    postInformations : WritePostForTradingViewModel
){
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ){
        Text(
            text = "거래 방식",
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.size(16.dp))
        OutlinedTextField(
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth(),
            maxLines = 1,
            placeholder = {
                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = "장소 선택",
                        color = Color(0xFF858C94)
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                        tint = Color(0xFF858C94),
                        contentDescription = "select the location Icon"
                    )
                }
            },
            value = postInformations.tradingLocation.value,
            onValueChange = { input ->
                postInformations.tradingLocation.value = input
            }
        )
    }
}

@Composable
fun WritePostForTradingScreenFooter(){
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonColors(
                containerColor = Color(0xFFFF6E1D),
                contentColor = Color.White,
                disabledContainerColor = Color(0xFFFF6E1D),
                disabledContentColor = Color.White
            ),
            onClick = {

            },
        ) {
            Text(
                text = "작성 완료",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(vertical = 4.dp)
            )
        }
    }
}