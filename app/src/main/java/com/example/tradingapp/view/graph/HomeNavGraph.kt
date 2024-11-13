package com.example.tradingapp.view.graph

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.tradingapp.model.data.navigation.HomeBottomNavigationBarItem
import com.example.tradingapp.model.data.navigation.HomeNavigationGraph
import com.example.tradingapp.model.data.navigation.valueOfHomeNavigationGraph
import com.example.tradingapp.model.data.post.PostDetails
import com.example.tradingapp.model.data.user.UserInformation
import com.example.tradingapp.model.viewmodel.home.HomeViewModel
import com.example.tradingapp.view.home.ChatView
import com.example.tradingapp.view.home.HomeView
import com.example.tradingapp.view.home.MapView
import com.example.tradingapp.view.home.MyProfileView
import com.example.tradingapp.view.post.community.CommunityView

@Composable
fun HomeNavGraph(
    myInformation : UserInformation,
    homeViewModel : HomeViewModel = viewModel(),
    homeNavController : NavHostController = rememberNavController(),
    selectedPost : (PostDetails) -> Unit,
    navigateFromMain : (route : String) -> Unit
){
    val currentContext = LocalContext.current
    var transitionDir by rememberSaveable { mutableStateOf(2) }
    var isDeactivatedScreen by remember { mutableStateOf(false) }
    val deactivatedScreenColor : Color by animateColorAsState(targetValue =
        if (isDeactivatedScreen){
            Color(0xC0000000)
        }
        else{
            Color(0x00000000)
        }
    )

    BackHandler ( enabled = true ){
        (currentContext as Activity).finish()
    }

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
                    homeViewModel,
                    isDeactivatedScreen,
                    setDeactivatedScreen = { getDeactivateScreen ->
                        isDeactivatedScreen = getDeactivateScreen
                    },
                    selectedPostDetails = { getSelectedPostDetails ->
                        selectedPost(getSelectedPostDetails)
                    },
                    navigateFromMain = { route ->
                        navigateFromMain(route)
                    }
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

                MyProfileView(tag, myInformation){ route -> navigateFromMain(route) }
            }
        }


        // footer
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
        ) {
            // Bottom navigation bar
            HomeBottomNavigationBar(homeNavController = homeNavController) { getTransitionDir ->
                transitionDir = getTransitionDir
            }

            if (isDeactivatedScreen){
                Surface (
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            isDeactivatedScreen = !isDeactivatedScreen
                        },
                    color = deactivatedScreenColor
                ) {}
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
    val navBackStackEntry by homeNavController.currentBackStackEntryAsState()
    val currentDestinationView = navBackStackEntry?.destination?.route
    var isCurrentView : Boolean

    Column (
        modifier = Modifier.wrapContentSize()
    ){
        HorizontalDivider( thickness = (1f).dp, color = Color(0xFF636365) )
        NavigationBar (
            modifier = Modifier.fillMaxWidth(),
            containerColor = Color(0xFF212123)
        ){
            navigationItems.forEach { item ->
                isCurrentView = item.route.equals(currentDestinationView)

                NavigationBarItem(
                    selected = isCurrentView,
                    onClick = {
                        if (currentDestinationView != null){
                            if (!currentDestinationView.equals(item.route)){
                                getTransitionDir(getTransitionDir(currentDestinationView!!, item.route))
                                homeNavController.popBackStack()
                                homeNavController.navigate(item.route)
                            }
                        }
                    },
                    icon = {
                        if (isCurrentView){
                            Icon(
                                painter = painterResource(id = item.selectedIcon),
                                contentDescription = item.title
                            )
                        }
                        else{
                            Icon(
                                painter = painterResource(id = item.normalIcon),
                                contentDescription = item.title
                            )
                        }
                    },
                    label = {
                        if (isCurrentView){
                            Text(
                                text = item.title,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        else{
                            Text(
                                text = item.title,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    },
                    alwaysShowLabel = true,
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = Color.White,
                        selectedIconColor = Color.White,
                        indicatorColor = Color(0x00333335),
                        unselectedTextColor = Color.White,
                        selectedTextColor = Color.White
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