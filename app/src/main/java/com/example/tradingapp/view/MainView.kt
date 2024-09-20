package com.example.tradingapp.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.tradingapp.R

enum class MainScreensList(){
    HOME,
    COMMUNITY,
    MAP,
    CHATS,
    PROFILE
}

sealed class MainScreenBottomNavigationItem(
    val title : String,
    val normalIcon : Int,
    val selectedIcon : Int,
    val route : String
){
    object Home : MainScreenBottomNavigationItem(
        title = "홈",
        normalIcon = R.drawable.outline_home_24,
        selectedIcon = R.drawable.baseline_home_24,
        route = MainScreensList.HOME.name
    )
    object Community : MainScreenBottomNavigationItem(
        title = "동네생활",
        normalIcon = R.drawable.outline_cell_tower_24,
        selectedIcon = R.drawable.outline_cell_tower_24,
        route = MainScreensList.COMMUNITY.name
    )
    object Map : MainScreenBottomNavigationItem(
        title = "동네지도",
        normalIcon = R.drawable.outline_place_24,
        selectedIcon = R.drawable.baseline_place_24,
        route = MainScreensList.MAP.name
    )
    object Chat : MainScreenBottomNavigationItem(
        title = "채팅",
        normalIcon = R.drawable.outline_forum_24,
        selectedIcon = R.drawable.baseline_forum_24,
        route = MainScreensList.CHATS.name
    )
    object Profile : MainScreenBottomNavigationItem(
        title = "나의 당근",
        normalIcon = R.drawable.baseline_person_outline_24,
        selectedIcon = R.drawable.baseline_person_24,
        route = MainScreensList.PROFILE.name
    )
}

@Composable
fun MainNavigationGraph(navController : NavHostController){
    NavHost(
        navController = navController,
        startDestination = MainScreensList.HOME.name
    ) {
        composable(route = MainScreensList.HOME.name){
            HomeScreen()
        }
        composable(route = MainScreensList.COMMUNITY.name){
            CommunityScreen()
        }
        composable(route = MainScreensList.MAP.name){
            MapScreen()
        }
        composable(route = MainScreensList.CHATS.name){
            ChatScreen()
        }
        composable(route = MainScreensList.PROFILE.name){
            ProfileScreen()
        }
    }
}

@Composable
fun MainBottomNavigation(
    navController : NavHostController,
    navigationItems : List<MainScreenBottomNavigationItem>
){
    var checkIsCurrentScreen : Boolean

    NavigationBar (
        modifier = Modifier
            .fillMaxWidth(),
        containerColor = Color(0xFF333335)
    ){
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestinationScreen = navBackStackEntry?.destination?.route

        navigationItems.forEach { item ->
            checkIsCurrentScreen = item.route.equals(currentDestinationScreen)

            NavigationBarItem(
                selected = checkIsCurrentScreen,
                onClick = {
                    navController.navigate(item.route)
                },
                icon = {
                    if (checkIsCurrentScreen){
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
                    if (checkIsCurrentScreen){
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

@Preview
@Composable
fun MainView(){
    val navigationItems = listOf<MainScreenBottomNavigationItem>(
        MainScreenBottomNavigationItem.Home,
        MainScreenBottomNavigationItem.Community,
        MainScreenBottomNavigationItem.Map,
        MainScreenBottomNavigationItem.Chat,
        MainScreenBottomNavigationItem.Profile
    )
    val navController = rememberNavController()

    Column (
        modifier = Modifier
            .fillMaxSize()
    ){
        // header & body
        Surface (
            modifier = Modifier
                .weight(1f)
        ){
            MainNavigationGraph(navController = navController)
        }

        // footer
        Column (
            modifier = Modifier
                .wrapContentSize()
        ){
            HorizontalDivider(
                thickness = (1f).dp,
                color = Color(0xFF636365)
            )
            MainBottomNavigation(
                navController = navController,
                navigationItems = navigationItems
            )
        }
    }
}