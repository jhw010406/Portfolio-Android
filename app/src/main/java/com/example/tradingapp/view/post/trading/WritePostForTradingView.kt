package com.example.tradingapp.view.post.trading

import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import com.example.tradingapp.R
import com.example.tradingapp.model.data.post.Image
import com.example.tradingapp.model.data.navigation.MainNavigationGraph
import com.example.tradingapp.model.data.post.PostDetails
import com.example.tradingapp.model.viewmodel.post.WritePostForTradingViewModel
import com.example.tradingapp.model.viewmodel.other.addDelimiterToPrice
import com.example.tradingapp.model.viewmodel.other.selectOptimalContractForMedia
import com.example.tradingapp.model.viewmodel.post.getSelectedPostDetails
import com.example.tradingapp.view.LoadingView

@Composable
fun WritePostForTradingView(
    tag : String,
    isForUpdate: Boolean,
    myUID : Int,
    writePostForTradingViewModel : WritePostForTradingViewModel,
    mainNavController : NavHostController
){
    var loadedPostDetails by rememberSaveable { mutableStateOf(false) }
    val postId = writePostForTradingViewModel.postId!!

    BackHandler {
        mainNavController.previousBackStackEntry?.savedStateHandle?.remove<PostDetails>("post-details")
        mainNavController.popBackStack()
    }

    LaunchedEffect(Unit) {

        if (!loadedPostDetails) {

            if (isForUpdate) {
                getSelectedPostDetails(tag, postId) { getPostDetails, isSuccessful ->

                    if (isSuccessful) {
                        getPostDetails?.let {
                            writePostForTradingViewModel.setInitialValues(
                                it.postID,
                                it.title,
                                it.content!!,
                                it.postDetailsTrading!!.productPrice,
                                it.postDetailsTrading.productImagesForGet!!
                            )
                        }
                    }

                    loadedPostDetails = true
                }
            }
        }
    }

    if (isForUpdate && !loadedPostDetails) {
        LoadingView()
    }
    else {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFF212123))
        ){
            LazyColumn ( modifier = Modifier.weight(1f) ) {
                // header
                item {
                    WritePostForTradingScreenHeader(mainNavController)
                    HorizontalDivider(
                        thickness = 1.dp,
                        modifier = Modifier.padding(horizontal = 0.dp),
                        color = Color(0xFF636365)
                    )
                }

                // body
                item {
                    WritePostForTradingScreenBody(tag, isForUpdate, writePostForTradingViewModel)
                }
            }

            // footer
            Surface (
                modifier = Modifier
                    .padding(horizontal = 12.dp),
                color = Color(0xFF212123)
            ){
                WritePostForTradingScreenFooter(
                    tag,
                    isForUpdate,
                    myUID,
                    writePostForTradingViewModel,
                    mainNavController
                )
            }
        }
    }
}

@Composable
fun WritePostForTradingScreenHeader(
    mainNavController : NavHostController
){
    val navBackStackEntry by mainNavController.currentBackStackEntryAsState()
    val currentDestinationView = navBackStackEntry?.destination?.route

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.CenterVertically)
            .background(color = Color(0xFF212123))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ){
        Row (
            modifier = Modifier
                .clickable {
                    if (currentDestinationView.equals(MainNavigationGraph.WRITEPOSTFORTRADING.name)){
                        mainNavController.popBackStack()
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
    }
}

@Composable
fun WritePostForTradingScreenBody(
    tag : String,
    isForUpdate : Boolean,
    postDetails : WritePostForTradingViewModel
){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp)
    ){
        // upload images
        Spacer(modifier = Modifier.size(8.dp))
        UploadImagesForTrading(tag, isForUpdate, postDetails)
        Spacer(modifier = Modifier.size(32.dp))

        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ){
            // write title
            WritePostTitle(postDetails)
            Spacer(modifier = Modifier.size(32.dp))

            // write how to trade
            SelectHowToTrade(postDetails)
            Spacer(modifier = Modifier.size(32.dp))

            // write details
            WriteProductDetails(postDetails)
            Spacer(modifier = Modifier.size(32.dp))

            // write the location that trading will be done
            SelectTradingLocation(postDetails)
            Spacer(modifier = Modifier.size(32.dp))
        }
    }
}

@Composable
fun UploadImagesForTrading(
    tag : String,
    isForUpdate : Boolean,
    postDetails : WritePostForTradingViewModel
){
    val currentContext = LocalContext.current
    val contentResolver = LocalContext.current.contentResolver
    var currentImagesCount = postDetails.selectedImages.size
    val getMultipleImageLauncher = rememberLauncherForActivityResult(
        contract = selectOptimalContractForMedia(currentImagesCount)
    ) { any : Any? ->

        if (any is List<*>){
            any.forEach { it : Any? -> it as Uri
                // 카운팅 먼저 하고 난 후, pre-signed url 획득 실패시 다시 차감
                // 비 동기로 인해 서버 응답이 느려질 수 있음을 고려한 방식
                currentImagesCount++
                postDetails.addImageToList(tag, it, currentContext, currentImagesCount){ getCurrentImagesCount ->
                    currentImagesCount = getCurrentImagesCount
                }
            }
        }
        else{ any as Uri
            currentImagesCount++
            postDetails.addImageToList(tag, any, currentContext, currentImagesCount) { getCurrentImagesCount ->
                currentImagesCount = getCurrentImagesCount
            }
        }

        Log.d(tag, "current images count : ${currentImagesCount}")
    }
    var imageCountTextColor : Color

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
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
                        if (postDetails.selectedImages.size < 10) {
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
                    if (postDetails.selectedImages.size > 0){
                        imageCountTextColor = Color(0xFFFF6E1D)
                    }
                    else{
                        imageCountTextColor = Color(0xFF858C94)
                    }

                    Text(
                        text = "${postDetails.selectedImages.size}",
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
                items = postDetails.selectedImages.toList(),
                key = { index: Int, image: Image? ->
                    index
                }
            ){  index: Int, image: Image ->
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
                        if (image.file != null) {
                            AsyncImage(
                                model = image.file!!.getUri(),
                                contentDescription = "product image"
                            )
                        }
                        else {
                            AsyncImage(
                                model = image.url,
                                contentDescription = "product image"
                            )
                        }
                    }

                    // Remove image button
                    Surface (
                        modifier = Modifier
                            .size(20.dp)
                            .offset(x = 56.dp, y = -8.dp)
                            .clickable {
                                // remove this image
                                postDetails.deleteImageFromList(
                                    tag,
                                    isForUpdate,
                                    image
                                ) { isSuccessful ->
                                    if (isSuccessful) {
                                        currentImagesCount--
                                    }
                                }
                                Log.d(tag, "current images count : ${currentImagesCount}")
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
    postDetails : WritePostForTradingViewModel
){
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ){
        Text(text = "제목", fontWeight = FontWeight.Bold, color = Color.White)
        Spacer(modifier = Modifier.size(16.dp))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            maxLines = 1,
            placeholder = { Text(text = "제목", color = Color(0xFF858C94)) },
            value = postDetails.title.value,
            onValueChange = { input ->
                if (!input.any{ char -> char == '\n' }){
                    postDetails.title.value = input
                }
            }
        )
    }
}

@Composable
fun SelectHowToTrade(
    postDetails : WritePostForTradingViewModel
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
            if (postDetails.forSelling.value){
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
                onClick = { postDetails.forSelling.value = true },
                colors = ButtonColors(
                    containerColor = toSellButtonColor,
                    contentColor = toSellButtonTextColor,
                    disabledContainerColor = Color(0xFF212123),
                    disabledContentColor = Color.White
                ),
                border = toSellButtonBorder
            ) { Text(text = "판매하기", fontSize = 16.sp) }
            Spacer(modifier = Modifier.size(8.dp))

            // to give
            Button(
                onClick = { postDetails.forSelling.value = false },
                colors = ButtonColors(
                    containerColor = toGiveButtonColor,
                    contentColor = toGiveButtonTextColor,
                    disabledContainerColor = Color(0xFF212123),
                    disabledContentColor = Color.White
                ),
                border = toGiveButtonBorder
            ) { Text(text = "나눔하기", fontSize = 16.sp) }
        }
        Spacer(modifier = Modifier.size(16.dp))

        // Sell
        if (postDetails.forSelling.value){
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged {
                        if (it.isFocused) {

                            if (postDetails.productPriceInt > 0) {
                                postDetails.productPrice.value =
                                    postDetails.productPriceInt.toString()
                            }
                        } else {
                            postDetails.productPrice.value =
                                addDelimiterToPrice(postDetails.productPriceInt.toString())
                        }
                    },
                maxLines = 1,
                placeholder = { Text(text = "가격을 입력해주세요.", color = Color(0xFF858C94)) },
                prefix = { Text(text = "₩ ", color = Color(0xFF858C94)) },
                value = postDetails.productPrice.value,
                onValueChange = { input ->
                    if (
                        input.all { char -> char.isDigit() } &&
                        input.length <= 9
                        ){
                        postDetails.productPrice.value = postDetails.getValidPrice(input)
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            Spacer(modifier = Modifier.size(16.dp))

            // accept other price request
            Row (
                modifier = Modifier
                    .wrapContentSize()
                    .clickable {
                        postDetails.acceptNegotiation.value =
                            !postDetails.acceptNegotiation.value
                    },
                verticalAlignment = Alignment.CenterVertically
            ){
                Checkbox(
                    modifier = Modifier.size(20.dp),
                    checked = postDetails.acceptNegotiation.value,
                    onCheckedChange = { input ->
                        postDetails.acceptNegotiation.value = input
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
                        postDetails.acceptFreebieRequest.value =
                            !postDetails.acceptFreebieRequest.value
                    },
                verticalAlignment = Alignment.CenterVertically
            ){
                Checkbox(
                    modifier = Modifier.size(20.dp),
                    checked = postDetails.acceptFreebieRequest.value,
                    onCheckedChange = { input ->
                        postDetails.acceptFreebieRequest.value = input
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
        Log.d("forSelling", "${postDetails.forSelling}")
    }
}

@Composable
fun WriteProductDetails(
    postDetails : WritePostForTradingViewModel
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
                .fillMaxWidth()
                .wrapContentHeight()
                .defaultMinSize(minHeight = 184.dp),
            placeholder = {
                Text(
                    text = "~에 올릴 게시글 내용을 작성해 주세요.\n" +
                            "(판매 금지 물품은 게시가 제한될 수 있어요.)\n\n" +
                            "신뢰할 수 있는 거래를 위해 자세히 적어주세요.\n" +
                            "과학기술정보통신부, 한국 인터넷진흥원과 함께 해요.",
                    color = Color(0xFF858C94)
                )
            },
            value = postDetails.content.value,
            onValueChange = { input ->
                postDetails.content.value = input
            }
        )
    }
}

@Composable
fun SelectTradingLocation(
    postDetails : WritePostForTradingViewModel
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
            modifier = Modifier.fillMaxWidth(),
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
            value = postDetails.location.value,
            onValueChange = { input ->
                postDetails.location.value = input
            }
        )
    }
}

@Composable
fun WritePostForTradingScreenFooter(
    tag : String,
    isForUpdate : Boolean,
    myUid : Int,
    postDetails : WritePostForTradingViewModel,
    mainNavController: NavHostController
){
    val currentContext = LocalContext.current
    var pressedUploadButton by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect (pressedUploadButton) {

        if (pressedUploadButton) {

            if (isForUpdate) {
                postDetails.updatePostForTrading(tag, postDetails.postId!!, myUid) { isSuccessful ->

                    if (isSuccessful) {
                        Toast.makeText(currentContext, "게시글 수정 완료", LENGTH_SHORT).show()
                        mainNavController.popBackStack()
                    }
                    else {
                        Toast.makeText(currentContext, "서버 에러", LENGTH_SHORT).show()
                    }
                }
            }
            else {
                postDetails.uploadPostForTrading(tag, myUid) { isSuccessful ->

                    if (isSuccessful) {
                        Toast.makeText(currentContext, "게시글 작성 완료", LENGTH_SHORT).show()
                        mainNavController.popBackStack()
                    }
                    else {
                        Toast.makeText(currentContext, "서버 에러", LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

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
                disabledContainerColor = Color(0xFFA26847),
                disabledContentColor = Color(0xFFA2A2A2)
            ),
            enabled = postDetails.isValidPostDetails(),
            onClick = { pressedUploadButton = true },
        ) {
            Text(
                text = "작성 완료",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}