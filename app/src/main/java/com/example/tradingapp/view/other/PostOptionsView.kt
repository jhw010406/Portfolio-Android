package com.example.tradingapp.view.other

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.tradingapp.model.data.navigation.MainNavigationGraph
import com.example.tradingapp.viewmodel.post.deletePost
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object PostOptionsPanel{
    // flow를 통해 값 변경시, 해당 변수를 참조하는 변수들에 대하여 지속적으로 update
    private val _showPanel = MutableStateFlow(false)
    // 상태를 반영하는 flow로 flow를 불러들임
    val isPressedButton : StateFlow<Boolean> = _showPanel.asStateFlow()
    var myUid : Int? = null
    var posterUid : Int? = null
    var postId : Int? = null

    fun show(
        inputMyId : Int,
        inputPosterId : Int,
        inputPostId : Int
    ) {
        _showPanel.value = true
        myUid = inputMyId
        posterUid = inputPosterId
        postId = inputPostId
    }

    fun hide() {
        _showPanel.value = false
        myUid = null
        posterUid = null
        postId = null
    }
}

@Composable
fun PostOptionsView(
    tag : String = "POST_OPTIONS_VIEW",
    mainNavController : NavHostController
) {
    // stateflow의 값을 확인
    val activatePostOptionsView = PostOptionsPanel.isPressedButton.collectAsState().value

    if (activatePostOptionsView) {
        val currentContext = LocalContext.current
        val basePanelColor = MaterialTheme.colorScheme.secondary
        val panelColor = Color(
            basePanelColor.red + 0.03f,
            basePanelColor.green + 0.03f,
            basePanelColor.blue + 0.03f
        )
        var pressedModifyButton by rememberSaveable { mutableStateOf(false) }
        var pressedDeleteButton by rememberSaveable { mutableStateOf(false) }
        var closePanel by rememberSaveable { mutableStateOf(true) }
        val fadeInOutBackground by animateFloatAsState(targetValue = if (!closePanel) { 0.5f } else { 0.0f })
        val movePanel by animateIntAsState(targetValue = if (!closePanel){ 0 } else { 460 }){
            if (closePanel) { PostOptionsPanel.hide() }
        }

        BackHandler { closePanel = true }

        LaunchedEffect (Unit) {

            if (closePanel) { closePanel = false }
        }

        LaunchedEffect(pressedDeleteButton) {

            if (pressedDeleteButton) {
                closePanel = true

                if (PostOptionsPanel.posterUid == PostOptionsPanel.myUid) {
                    deletePost(tag, PostOptionsPanel.postId!!) { isSuccessful ->

                        if (isSuccessful) {
                            RootSnackbar.show("게시글 삭제 성공")
                        }
                        else {
                            RootSnackbar.show("게시글 삭제 실패. 다시 시도해주세요.")
                        }
                    }
                }
                else {
                    RootSnackbar.show("다른 유저의 글을 삭제할 수 없습니다.")
                }
            }
        }

        LaunchedEffect(pressedModifyButton) {

            if (pressedModifyButton) {
                closePanel = true
                if (PostOptionsPanel.posterUid == PostOptionsPanel.myUid) {
                    mainNavController.currentBackStackEntry?.savedStateHandle?.set("modify_post_id", PostOptionsPanel.postId)
                    mainNavController.navigate(MainNavigationGraph.WRITEPOSTFORTRADING.name)
                }
                else {
                    RootSnackbar.show("다른 유저의 글을 수정할 수 없습니다.")
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    drawRect(
                        size = this.size,
                        color = Color.Black,
                        alpha = fadeInOutBackground
                    )
                }
                .clickable(
                    interactionSource = null,
                    indication = null
                ) { closePanel = true },
            contentAlignment = Alignment.BottomCenter
        ) {
            Surface (
                modifier = Modifier
                    .fillMaxWidth()
                    .offset { IntOffset(x = 0, y = movePanel) },
                shape = RoundedCornerShape(10, 10, 0, 0),
                color = panelColor
            ) {
                Column {
                    Spacer(modifier = Modifier.size(8.dp))
                    Surface (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        shape = RoundedCornerShape(10)
                    ) {
                        Column {
                            Row (
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(
                                        enabled = (!pressedModifyButton && !pressedDeleteButton)
                                    ) { pressedModifyButton = true },
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(text = "글 수정하기", modifier = Modifier.padding(vertical = 8.dp))
                            }

                            HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(horizontal = 12.dp))

                            Row (
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(
                                        enabled = (!pressedModifyButton && !pressedDeleteButton)
                                    ) { pressedDeleteButton = true },
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(text = "게시글 삭제", color = Color(0xFFFF4000), modifier = Modifier.padding(vertical = 8.dp))
                            }
                        }
                    }

                    Surface (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                            .clickable { closePanel = true },
                        shape = RoundedCornerShape(10)
                    ) {
                        Text(text = "닫기", textAlign = TextAlign.Center, modifier = Modifier.padding(vertical = 8.dp))
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                }
            }
        }
    }
}