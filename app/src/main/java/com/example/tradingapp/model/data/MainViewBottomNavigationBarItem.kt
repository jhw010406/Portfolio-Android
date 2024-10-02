package com.example.tradingapp.model.data

import com.example.tradingapp.R

enum class MainViewBottomNavigationBarItemList(){
    HOME,
    COMMUNITY,
    MAP,
    CHATS,
    PROFILE
}

sealed class MainViewBottomNavigationBarItem(
    val title : String,
    val normalIcon : Int,
    val selectedIcon : Int,
    val route : String
){
    object Home : MainViewBottomNavigationBarItem(
        title = "홈",
        normalIcon = R.drawable.outline_home_24,
        selectedIcon = R.drawable.baseline_home_24,
        route = MainViewBottomNavigationBarItemList.HOME.name
    )
    object Community : MainViewBottomNavigationBarItem(
        title = "동네생활",
        normalIcon = R.drawable.outline_cell_tower_24,
        selectedIcon = R.drawable.outline_cell_tower_24,
        route = MainViewBottomNavigationBarItemList.COMMUNITY.name
    )
    object Map : MainViewBottomNavigationBarItem(
        title = "동네지도",
        normalIcon = R.drawable.outline_place_24,
        selectedIcon = R.drawable.baseline_place_24,
        route = MainViewBottomNavigationBarItemList.MAP.name
    )
    object Chat : MainViewBottomNavigationBarItem(
        title = "채팅",
        normalIcon = R.drawable.outline_forum_24,
        selectedIcon = R.drawable.baseline_forum_24,
        route = MainViewBottomNavigationBarItemList.CHATS.name
    )
    object Profile : MainViewBottomNavigationBarItem(
        title = "나의 당근",
        normalIcon = R.drawable.baseline_person_outline_24,
        selectedIcon = R.drawable.baseline_person_24,
        route = MainViewBottomNavigationBarItemList.PROFILE.name
    )
}
