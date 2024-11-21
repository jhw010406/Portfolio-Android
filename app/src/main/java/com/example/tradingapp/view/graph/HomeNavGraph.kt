package com.example.tradingapp.view.graph

import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.tradingapp.model.data.navigation.HomeBottomNavigationBarItem
import com.example.tradingapp.model.data.navigation.HomeNavigationGraph
import com.example.tradingapp.model.data.navigation.valueOfHomeNavigationGraph
import com.example.tradingapp.model.data.user.UserInformation
import com.example.tradingapp.viewmodel.home.HomeViewModel
import com.example.tradingapp.view.home.ChatView
import com.example.tradingapp.view.home.HomeView
import com.example.tradingapp.view.home.MapView
import com.example.tradingapp.view.home.MyProfileView
import com.example.tradingapp.view.other.LoadingBar
import com.example.tradingapp.view.post.community.CommunityView
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object HomeScreen{
    private val _deactiveHomeScreen = MutableStateFlow(false)
    val deactiveHomeScreen : StateFlow<Boolean> = _deactiveHomeScreen.asStateFlow()
    private val _deactivatedScreenAlpha = MutableStateFlow(0f)
    val deactivedScreenAlpha : StateFlow<Float> = _deactivatedScreenAlpha.asStateFlow()

    fun deactive(){
        _deactiveHomeScreen.value = true
    }

    fun active(){
        _deactiveHomeScreen.value = false
    }

    fun setDeactivedScreenAlpha(alpha : Float) {
        _deactivatedScreenAlpha.value = alpha
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeNavGraph(
    myInformation : UserInformation,
    homeViewModel : HomeViewModel,
    mainNavController : NavHostController,
    homeNavController : NavHostController = rememberNavController()
){
    val currentContext = LocalContext.current
    val homeGraphRootEntryId = homeViewModel.homeGraphRootEntryId.value
    var transitionDir by rememberSaveable { mutableStateOf(2) }
    val isDeactivatedHomeScreen = HomeScreen.deactiveHomeScreen.collectAsState().value

    BackHandler( enabled = true ){

        if (isDeactivatedHomeScreen) { HomeScreen.active() }
        else { (currentContext as Activity).finish() }
    }

    LaunchedEffect(Unit) {
        HomeScreen.active()

        if (homeGraphRootEntryId == null) {
            homeViewModel.homeGraphRootEntryId.value = mainNavController.currentBackStackEntry?.id
        }
    }

    if (homeGraphRootEntryId == null) {
        LoadingBar.show()
    }
    else {
        LoadingBar.hide()
        Column (
            modifier = Modifier.fillMaxSize()
        ){
            // header & body
            NavHost(
                modifier = Modifier.weight(1f),
                navController = homeNavController,
                startDestination = HomeNavigationGraph.HOME.name,
                enterTransition = { slideInHorizontally(initialOffsetX = { it / transitionDir }) },
                exitTransition = { slideOutHorizontally(targetOffsetX = { it / (-transitionDir) }) },
                popEnterTransition = { EnterTransition.None },
                popExitTransition = { ExitTransition.None }
            ) {
                composable(route = HomeNavigationGraph.HOME.name){
                    val tag = "HOME_VIEW"

                    HomeView(
                        tag,
                        myInformation,
                        homeViewModel,
                        homeGraphRootEntryId,
                        homeNavController,
                        mainNavController
                    )
                }
                composable(route = HomeNavigationGraph.COMMUNITY.name){
                    val tag = "COMMUNITY_VIEW"

                    CommunityView(tag)
                }
                composable(route = HomeNavigationGraph.MAP.name){
                    val tag = "MAP_VIEW"

                    MapView(tag)
                }
                composable(route = HomeNavigationGraph.CHATS.name){
                    val tag = "CHAT_VIEW"

                    ChatView(tag)
                }
                composable(route = HomeNavigationGraph.PROFILE.name){
                    val tag = "MY_PROFILE_VIEW"
                    Log.d(tag, "${myInformation}")
                    MyProfileView(tag, myInformation, mainNavController)
                }
            }


            // footer
            // Bottom navigation bar
            HomeBottomNavigationBar(homeNavController) { getTransitionDir ->
                transitionDir = getTransitionDir
            }
        }
    }
}

@Composable
fun HomeBottomNavigationBar(
    homeNavController : NavHostController,
    navigationItems : List<HomeBottomNavigationBarItem> = listOf(
        HomeBottomNavigationBarItem.Home,
        HomeBottomNavigationBarItem.Community,
        HomeBottomNavigationBarItem.Map,
        HomeBottomNavigationBarItem.Chat,
        HomeBottomNavigationBarItem.Profile
    ),
    getTransitionDir : (Int) -> Unit
){
    val isDeactivatedHomeScreen = HomeScreen.deactiveHomeScreen.collectAsState().value
    val deactivatedScreenAlpha by animateFloatAsState(
        targetValue = if (isDeactivatedHomeScreen){ 0.5f } else{ 0.0f }
    )
    val currentRoute = homeNavController.currentBackStackEntryAsState().value?.destination?.route
    var isCurrentView : Boolean

    Column (
        modifier = Modifier
            .wrapContentSize()
            .height(80.dp)
            .drawWithContent {
                drawContent()
                drawRect(
                    color = Color.Black,
                    alpha = deactivatedScreenAlpha
                )
            }
    ){
        HorizontalDivider( thickness = (1f).dp )
        NavigationBar (
            modifier = Modifier.fillMaxWidth()
        ){
            navigationItems.forEach { item ->
                isCurrentView = (item.route == currentRoute)

                NavigationBarItem(
                    selected = isCurrentView,
                    onClick = {
                        if (!isDeactivatedHomeScreen
                            && currentRoute != null){

                            if (currentRoute != item.route){
                                getTransitionDir(getTransitionDir(currentRoute, item.route))
                                homeNavController.popBackStack()
                                homeNavController.navigate(item.route)
                                // 임의 버튼과 동시 클릭한 경우를 고려
                                HomeScreen.active()
                            }
                        }
                        else { HomeScreen.active() }
                    },
                    icon = {
                        if (isCurrentView){
                            Icon(painter = painterResource(id = item.selectedIcon), tint = MaterialTheme.colorScheme.onSurface, contentDescription = item.title)
                        }
                        else{
                            Icon(painter = painterResource(id = item.normalIcon), tint = MaterialTheme.colorScheme.onSurface, contentDescription = item.title)
                        }
                    },
                    label = {
                        if (isCurrentView){ Text(text = item.title, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold) }
                        else{ Text(text = item.title, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Normal) }
                    },
                    alwaysShowLabel = true,
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        }
    }
}

fun getTransitionDir(
    currentRoute : String,
    targetRoute : String,
) : Int {
    val currentRouteIdx : Int? = valueOfHomeNavigationGraph.get(currentRoute)
    val targetRouteIdx : Int? = valueOfHomeNavigationGraph.get(targetRoute)

    if ((currentRouteIdx != null) && (targetRouteIdx != null)){
        if (targetRouteIdx < currentRouteIdx){ return (-1) }
    }

    return (1)
}