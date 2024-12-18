package com.example.tradingapp.view.post.community

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tradingapp.R
import com.example.tradingapp.model.data.chat.Chat
import com.example.tradingapp.model.data.post.PostDetails
import com.example.tradingapp.model.data.post.PostSubject
import com.example.tradingapp.viewmodel.chat.ChatViewModel
import com.example.tradingapp.viewmodel.home.CommunityViewModel

@Composable
fun CommunityView(
    tag : String
){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary)
    ) {
        // categories List
        CommunityViewHeader()
        // body
        CommunityViewBody()
    }
}

@Composable
fun CommunityViewHeader(){
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.CenterVertically)
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            // location
            Row (
                modifier = Modifier
                    .clickable(
                        interactionSource = null,
                        indication = null
                    ) {},
                horizontalArrangement = Arrangement.Start
            ){
                Text(text = "location",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.size(4.dp))
                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                    contentDescription = "select living location",
                    modifier = Modifier.rotate(-90f).offset(x = 4.dp)
                )
            }

            // others
            Row (horizontalArrangement = Arrangement.Center){
                // my profile
                Icon(painter = painterResource(id = R.drawable.outline_sentiment_satisfied_24),
                    contentDescription = "edit profile",
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.size(16.dp))

                // search product
                Icon(painter = painterResource(id = R.drawable.baseline_search_24),
                    contentDescription = "search product",
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.size(16.dp))

                // check notification
                Icon(painter = painterResource(id = R.drawable.baseline_notifications_none_24),
                    contentDescription = "check notification",
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
    HorizontalDivider(thickness = 1.dp)
}

@Composable
fun CommunityViewBody(){
    val communityViewModel : CommunityViewModel = viewModel()

    LazyColumn{
        item(
            key = "groupChatList"
        ){
            PreviewGroupChatList()
            Spacer(modifier = Modifier.size(8.dp))
        }

        // subject list
        item(
            key = "subjectList"
        ){
            CommunityCategoriesList()
        }

        // Divider line
        item (
            key = "line"
        ){
            HorizontalDivider(thickness = 1.dp)
        }

        // posts list
        item (
            key = "postList"
        ){
            Surface (
                color = Color(0xFF212123)
            ){
                Column (
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                ){
                    /*
                    communityViewModel.GetCommunityPostList().forEach { post ->
                        Surface (
                            modifier = Modifier
                                .wrapContentSize(),
                            color = Color(0xFF212123),
                            onClick = {

                            }
                        ){
                            PreviewCommunityPostsList(post)
                        }
                    }*/
                }
            }
        }
    }
}

@Composable
fun PreviewGroupChatList(){
    val groupChatViewModel : ChatViewModel = viewModel()

    Log.d("theme_color", "${Color.White}\n${MaterialTheme.colorScheme.surface}")

    NavigationBar (
        modifier = Modifier.fillMaxWidth().wrapContentHeight()
    ){
        LazyRow (
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 12.dp)
        ){
            item {
                Spacer(modifier = Modifier.size(8.dp))
                FindOtherGroupsItem()
            }

            item {
                VerticalDivider(modifier = Modifier.height(68.dp), thickness = 1.dp)
                Spacer(modifier = Modifier.size(12.dp))
            }

            itemsIndexed(
                items = groupChatViewModel.GetPreviewGroupChatList(),
                key = { index: Int, item: Chat ->
                    item.id
                }
            ) { index: Int, item: Chat ->
                PreviewGroupChatItem(groupChat = item)
            }
        }
    }
}

class FindOtherGroupsIconShape : androidx.compose.ui.graphics.Shape {
    override fun createOutline(
        size: androidx.compose.ui.geometry.Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            addOval(
                Rect(
                    left = size.width / 20f,
                    top = size.height / 8f,
                    right = size.width * 19f / 20f,
                    bottom = size.height * 3f / 4f
                )
            )
        }
        return Outline.Generic(path = path)
    }
}

@Composable
fun FindOtherGroupsItem(){
    Row (
        modifier = Modifier.wrapContentSize()
    ){
        Column (
            modifier = Modifier
                .width(72.dp)
                .height(88.dp)
                .clickable {},
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ){
            Surface(modifier = Modifier.size(48.dp)){
                Icon(
                    painter = painterResource(id = R.drawable.baseline_group_24),
                    tint = Color(0xFFEA893F),
                    modifier = Modifier.clip(FindOtherGroupsIconShape()),
                    contentDescription = "search other groups"
                )
            }
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = "모임 둘러보기",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 10.sp
            )
        }
        Spacer(modifier = Modifier.size(12.dp))
    }
}

@Composable
fun PreviewGroupChatItem(groupChat : Chat){
    Row (modifier = Modifier.wrapContentSize()){
        Column (
            modifier = Modifier
                .width(72.dp)
                .height(88.dp)
                .clickable {},
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ){
            Box(modifier = Modifier.size(48.dp)){
                Surface (
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(12.dp)
                ){
                    Text(text = "Image")
                }

                Surface (
                    modifier = Modifier
                        .size(20.dp)
                        .offset(x = 32.dp, y = 32.dp),
                    shape = CircleShape,
                    color = Color(0xFFB2B6BF),
                    border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.surface)
                ){
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_add_24),
                        tint = Color.White,
                        modifier = Modifier.padding(4.dp),
                        contentDescription = "add Icon"
                    )
                }
            }
            Spacer(modifier = Modifier.size(8.dp))

            Text(
                text = groupChat.title,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                softWrap = true,
                lineHeight = 12.sp,
                maxLines = 2,
                fontSize = 10.sp,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.size(12.dp))
    }
}

@Composable
fun CommunityCategoriesList(){
    NavigationBar (modifier = Modifier.fillMaxWidth().height(64.dp)){
        val betweenSpace : Float = 8f

        LazyRow (
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(align = Alignment.CenterVertically)
        ){
            // subject menu
            item {
                Spacer(modifier = Modifier.size(12.dp))
                CommunitySubjectButton(
                    subjectName = "메뉴",
                    iconID = R.drawable.baseline_menu_24
                )
            }

            // trending post
            item {
                Spacer(modifier = Modifier.size(betweenSpace.dp))
                CommunitySubjectButton(
                    subjectName = "인기글",
                    iconID = R.drawable.baseline_local_fire_department_24
                )
            }

            items(
                count = PostSubject.townInformation.subjects.size
            ){  index: Int ->
                Spacer(modifier = Modifier.size(betweenSpace.dp))
                CommunitySubjectButton(
                    subjectName = PostSubject.townInformation.subjects[index],
                    iconID = null
                )
            }

            items(
                count = PostSubject.withNeighbor.subjects.size
            ){  index: Int ->
                Spacer(modifier = Modifier.size(betweenSpace.dp))
                CommunitySubjectButton(
                    subjectName = PostSubject.withNeighbor.subjects[index],
                    iconID = null
                )
            }

            items(
                count = PostSubject.news.subjects.size
            ){  index: Int ->
                Spacer(modifier = Modifier.size(betweenSpace.dp))
                CommunitySubjectButton(
                    subjectName = PostSubject.news.subjects[index],
                    iconID = null
                )
            }

            items(
                count = PostSubject.others.subjects.size
            ){  index: Int ->
                Spacer(modifier = Modifier.size(betweenSpace.dp))
                CommunitySubjectButton(
                    subjectName = PostSubject.others.subjects[index],
                    iconID = null
                )
            }

            item{
                Spacer(modifier = Modifier.size(12.dp))
            }
        }
    }
}

@Composable
fun CommunitySubjectButton(
    subjectName : String,
    iconID : Int?
){
    val colorValue : Color

    if (subjectName == "인기글") { colorValue = Color(0xFFFF5555) }
    else { colorValue = MaterialTheme.colorScheme.onPrimary }

    Surface (
        modifier = Modifier.wrapContentWidth().height(36.dp),
        color = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.outline),
        onClick = {}
    ){
        Row (
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ){
            if (iconID != null){
                Icon(
                    painter = painterResource(id = iconID),
                    modifier = Modifier.size(22.dp),
                    tint = colorValue,
                    contentDescription = subjectName
                )
                Spacer(modifier = Modifier.size(4.dp))
            }
            Text(text = subjectName, fontSize = 14.sp)
        }
    }
}

@Composable
fun PreviewCommunityPostsList(post : PostDetails){
    Column (
        modifier = Modifier.fillMaxWidth()
    ){
        Spacer(modifier = Modifier.size(16.dp))
        // The category of the post
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            // Is the post trending
            Surface (
                modifier = Modifier
                    .wrapContentWidth()
                    .fillMaxHeight(),
                shape = RoundedCornerShape(2.dp)
            ){
                /*
                if (post.othersForCommunity?.isTrendingPost == true){
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            text = "인기",
                            fontSize = 12.sp,
                            modifier = Modifier
                                .padding(horizontal = 4.dp, vertical = 2.dp),
                            style = TextStyle(
                                // 텍스트를 부모 align 규칙에 따라 정렬하기 위해 typograpy의 기본 정렬 기준을 비활성화한다.
                                platformStyle = PlatformTextStyle(
                                    includeFontPadding = false
                                )
                            ),
                            color = Color(0xFFA3D9F3)
                        )
                    }
                }*/
            }
            Spacer(modifier = Modifier.size(4.dp))

            // category
            Surface (
                modifier = Modifier
                    .wrapContentWidth()
                    .fillMaxHeight(),
                shape = RoundedCornerShape(2.dp),
                color = Color(0xFF434651)
            ){
                /*
                if (post.othersForCommunity != null){
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            text = post.othersForCommunity.subject,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .padding(horizontal = 4.dp, vertical = 2.dp),
                            style = TextStyle(
                                // 텍스트를 부모 align 규칙에 따라 정렬하기 위해 typograpy의 기본 정렬 기준을 비활성화한다.
                                platformStyle = PlatformTextStyle(
                                    includeFontPadding = false
                                )
                            ),
                            color = Color(0xFFADB0B5)
                        )
                    }
                }

                 */
            }
            Spacer(modifier = Modifier.size(4.dp))
        }
        Spacer(modifier = Modifier.size(8.dp))

        // title
        Text(
            text = post.title,
            fontSize = 16.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color.White
        )
        Spacer(modifier = Modifier.size(8.dp))

        // content
        Text(
            text = post.content!!,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color(0xFF8E8D91)
        )
        Spacer(modifier = Modifier.size(8.dp))

        // others
        Text(
            text = "${post.location} • ${post.uploadDate} • 조회 ${post.viewCount}",
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color(0xFF8E8D91)
        )
        Spacer(modifier = Modifier.size(8.dp))

        HorizontalDivider(thickness = 1.dp)
    }
}