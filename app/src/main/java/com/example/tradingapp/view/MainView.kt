package com.example.tradingapp.view

import android.util.Log
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.tradingapp.model.data.MainViewBottomNavigationBarItem
import com.example.tradingapp.model.data.MainViewBottomNavigationBarItemList
import com.example.tradingapp.model.data.OtherNavigationGraph

@Composable
fun MainNavigationGraph(
    navController : NavHostController,
    getDeactivatedScreen : Boolean,
    setDeactivatedScreen : (Boolean) -> Unit
){
    NavHost(
        navController = navController,
        startDestination = MainViewBottomNavigationBarItemList.HOME.name
    ) {
        composable(route = MainViewBottomNavigationBarItemList.HOME.name){
            HomeView(
                navController,
                getDeactivatedScreen,
                setDeactivatedScreen
            )
        }
        composable(route = MainViewBottomNavigationBarItemList.COMMUNITY.name){
            CommunityView()
        }
        composable(route = MainViewBottomNavigationBarItemList.MAP.name){
            MapView()
        }
        composable(route = MainViewBottomNavigationBarItemList.CHATS.name){
            ChatView()
        }
        composable(route = MainViewBottomNavigationBarItemList.PROFILE.name){
            ProfileView()
        }
        composable(route = OtherNavigationGraph.WRITEPOSTFORTRADING.name){
            WritePostForTradingScreen(navController)
        }
    }
}

@Composable
fun MainBottomNavigationBar(
    navController : NavHostController,
    navigationItems : List<MainViewBottomNavigationBarItem>
){
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestinationView = navBackStackEntry?.destination?.route
    var checkIsCurrentView : Boolean

    Column (
        modifier = Modifier
            .wrapContentSize()
    ){
        HorizontalDivider(
            thickness = (1f).dp,
            color = Color(0xFF636365)
        )
        NavigationBar (
            modifier = Modifier
                .fillMaxWidth(),
            containerColor = Color(0xFF212123)
        ){
            navigationItems.forEach { item ->
                checkIsCurrentView = item.route.equals(currentDestinationView)

                NavigationBarItem(
                    selected = checkIsCurrentView,
                    onClick = {
                        if (currentDestinationView != null){
                            if (!currentDestinationView.equals(item.route)){
                                navController.navigate(item.route){
                                    popUpTo(currentDestinationView){
                                        inclusive = true
                                    }
                                }
                            }
                        }
                    },
                    icon = {
                        if (checkIsCurrentView){
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
                        if (checkIsCurrentView){
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

fun CheckCurrentViewIsInMainView(
    currentDestinationView : String?,
    navigationItems : List<MainViewBottomNavigationBarItem>
) : Boolean {
    if (currentDestinationView == null){
        return false
    }
    navigationItems.forEach(){ item ->
        if (currentDestinationView.equals(item.route)){
            return true
        }
    }
    return false
}

@Preview
@Composable
fun MainView(
){
    val navigationItems = listOf<MainViewBottomNavigationBarItem>(
        MainViewBottomNavigationBarItem.Home,
        MainViewBottomNavigationBarItem.Community,
        MainViewBottomNavigationBarItem.Map,
        MainViewBottomNavigationBarItem.Chat,
        MainViewBottomNavigationBarItem.Profile
    )
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestinationView = navBackStackEntry?.destination?.route
    val deactivatedScreen = rememberSaveable {
        mutableStateOf(false)
    }
    val deactivatedScreenColor : Color by animateColorAsState(
        targetValue =
        if (deactivatedScreen.value){
            Color(0xC0000000)
        }
        else{
            Color(0x00000000)
        }
    )

    Column (
        modifier = Modifier
            .fillMaxSize()
    ){
        // header & body
        Box (
            modifier = Modifier
                .weight(1f)
        ){
            MainNavigationGraph(
                navController = navController,
                getDeactivatedScreen = deactivatedScreen.value,
                setDeactivatedScreen = { setDeactivatedScreen ->
                    deactivatedScreen.value = setDeactivatedScreen
                }
            )
        }

        // footer
        if (CheckCurrentViewIsInMainView(currentDestinationView, navigationItems)){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            ) {
                // Bottom navigation bar
                MainBottomNavigationBar(
                    navController = navController,
                    navigationItems = navigationItems
                )

                if (deactivatedScreen.value){
                    Surface (
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .clickable {
                                deactivatedScreen.value = !deactivatedScreen.value
                            },
                        color = deactivatedScreenColor
                    ) {}
                }
            }
        }
    }
}