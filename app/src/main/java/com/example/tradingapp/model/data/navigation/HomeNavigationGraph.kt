package com.example.tradingapp.model.data.navigation

import com.example.tradingapp.R

enum class HomeNavigationGraph(
    val value : Int
) {
    HOME(0),
    COMMUNITY(1),
    MAP(2),
    CHATS(3),
    PROFILE(4);
}

val valueOfHomeNavigationGraph : Map<String, Int> = mapOf(
    HomeNavigationGraph.HOME.name to HomeNavigationGraph.HOME.value,
    HomeNavigationGraph.COMMUNITY.name to HomeNavigationGraph.COMMUNITY.value,
    HomeNavigationGraph.MAP.name to HomeNavigationGraph.MAP.value,
    HomeNavigationGraph.CHATS.name to HomeNavigationGraph.CHATS.value,
    HomeNavigationGraph.PROFILE.name to HomeNavigationGraph.PROFILE.value
)

sealed class HomeBottomNavigationBarItem(
    val title : String,
    val normalIcon : Int,
    val selectedIcon : Int,
    val route : String
){
    object Home : HomeBottomNavigationBarItem(
        title = "홈",
        normalIcon = R.drawable.outline_home_24,
        selectedIcon = R.drawable.baseline_home_24,
        route = HomeNavigationGraph.HOME.name
    )
    object Community : HomeBottomNavigationBarItem(
        title = "동네생활",
        normalIcon = R.drawable.outline_cell_tower_24,
        selectedIcon = R.drawable.outline_cell_tower_24,
        route = HomeNavigationGraph.COMMUNITY.name
    )
    object Map : HomeBottomNavigationBarItem(
        title = "동네지도",
        normalIcon = R.drawable.outline_place_24,
        selectedIcon = R.drawable.baseline_place_24,
        route = HomeNavigationGraph.MAP.name
    )
    object Chat : HomeBottomNavigationBarItem(
        title = "채팅",
        normalIcon = R.drawable.outline_forum_24,
        selectedIcon = R.drawable.baseline_forum_24,
        route = HomeNavigationGraph.CHATS.name
    )
    object Profile : HomeBottomNavigationBarItem(
        title = "나의 당근",
        normalIcon = R.drawable.baseline_person_outline_24,
        selectedIcon = R.drawable.baseline_person_24,
        route = HomeNavigationGraph.PROFILE.name
    )
}
