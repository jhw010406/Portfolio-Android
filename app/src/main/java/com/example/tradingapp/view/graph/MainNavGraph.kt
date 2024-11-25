package com.example.tradingapp.view.graph

import android.os.Build
import android.util.Log
import android.view.ViewTreeObserver
import androidx.annotation.RequiresApi
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tradingapp.model.data.navigation.MainNavigationGraph
import com.example.tradingapp.viewmodel.home.HomeViewModel
import com.example.tradingapp.viewmodel.post.TradingPostViewModel
import com.example.tradingapp.viewmodel.post.WritePostForTradingViewModel
import com.example.tradingapp.viewmodel.verify.LoginViewModel
import com.example.tradingapp.viewmodel.verify.UserInformationViewModel
import com.example.tradingapp.view.other.LoadingBar
import com.example.tradingapp.view.other.LoadingView
import com.example.tradingapp.view.other.PostOptionsView
import com.example.tradingapp.view.other.RootSnackbar
import com.example.tradingapp.view.post.trading.FavoritePostsListView
import com.example.tradingapp.view.post.trading.TradingPostView
import com.example.tradingapp.view.post.trading.UserPostsListView
import com.example.tradingapp.view.post.trading.WritePostForTradingView
import com.example.tradingapp.view.profile.SettingOptionsView
import com.example.tradingapp.view.verify.LoginView
import com.example.tradingapp.view.verify.RegisterView
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavGraph (
    loginViewModel : LoginViewModel = viewModel(),
    myInfoViewModel : UserInformationViewModel = viewModel(),
    homeViewModel : HomeViewModel = viewModel(),
    mainNavController : NavHostController = rememberNavController()
) {
    val currentContext = LocalContext.current
    val isLoading = LoadingBar.isDisplayed.collectAsState().value

    Box (
        modifier = Modifier.fillMaxSize()
    ) {
        NavHost(
            modifier = Modifier.fillMaxSize(),
            navController = mainNavController,
            startDestination = MainNavigationGraph.LOGIN.name,
            enterTransition = { slideInVertically(initialOffsetY = { it / 2 }) + fadeIn() },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { slideOutVertically(targetOffsetY = { it }) + fadeOut() }
        ) {
            // login
            composable(route = MainNavigationGraph.LOGIN.name){
                val tag = "LOGIN_VIEW"

                LoginView(
                    tag = tag,
                    loginViewModel = loginViewModel,
                    myInfo = myInfoViewModel,
                    navController = mainNavController
                )
            }
            // register
            composable(route = MainNavigationGraph.REGISTER.name){
                val tag = "REGISTER_VIEW"

                RegisterView(
                    tag = tag,
                    loginViewModel = loginViewModel,
                    myInfo = myInfoViewModel,
                    mainNavController = mainNavController
                )
            }
            // home
            composable(route = MainNavigationGraph.HOME.name){
                HomeNavGraph (
                    myInformation = myInfoViewModel.userInfo.value!!,
                    homeViewModel = homeViewModel,
                    mainNavController = mainNavController
                )
            }
            composable(route = MainNavigationGraph.SETTINGOPTIONS.name) {
                val tag = "SETTING_OPTIONS_VIEW"

                SettingOptionsView(
                    tag = tag,
                    userCertificate = loginViewModel.myCertificate!!,
                    mainNavController = mainNavController
                )
            }
            composable(route = MainNavigationGraph.WRITEPOSTFORTRADING.name){
                val tag = "WRITE_POST_FOR_TRADING"
                val writePostForTradingViewModel : WritePostForTradingViewModel = viewModel()

                if (writePostForTradingViewModel.postId == null) {
                    writePostForTradingViewModel.postId = mainNavController
                            .previousBackStackEntry?.savedStateHandle?.get<Int>("modify_post_id") ?: (-1)

                    if (writePostForTradingViewModel.postId!! >= 0) {
                        mainNavController.previousBackStackEntry?.savedStateHandle?.remove<Int>("modify_post_id")
                    }
                }

                WritePostForTradingView(
                    tag = tag,
                    isForUpdate = (writePostForTradingViewModel.postId!! >= 0),
                    myUID = myInfoViewModel.userInfo.value!!.uid,
                    writePostForTradingViewModel = writePostForTradingViewModel,
                    mainNavController = mainNavController
                )
            }
            composable(route = MainNavigationGraph.TRADINGPOST.name){
                val tag = "TRADING_POST_VIEW"
                val tradingPostViewModel: TradingPostViewModel = viewModel()

                if (tradingPostViewModel.postId == null) {
                    tradingPostViewModel.postId = mainNavController.previousBackStackEntry?.savedStateHandle?.get("post_id")

                    if (tradingPostViewModel.postId != null) {
                        mainNavController.previousBackStackEntry?.savedStateHandle?.remove<Int>("post_id")
                    }
                }

                tradingPostViewModel.postId?.let {
                    TradingPostView(
                        tag = tag,
                        myInfo = myInfoViewModel.userInfo.value!!,
                        postId = it,
                        homeViewModel = homeViewModel,
                        tradingPostViewModel = tradingPostViewModel,
                        mainNavController = mainNavController
                    )
                }
            }
            composable(route = MainNavigationGraph.USERTRADINGPOSTSLIST.name){
                val tag = "USER_TRADING_POSTS_LIST_VIEW"
                val prevBackStackEntryBundle = mainNavController.previousBackStackEntry?.savedStateHandle
                var isMyPostsList by rememberSaveable { mutableStateOf(false) }
                var userId : String? by rememberSaveable { mutableStateOf(prevBackStackEntryBundle?.get("user_id")) }
                var userUid : Int? by rememberSaveable { mutableStateOf(prevBackStackEntryBundle?.get("user_uid")) }

                LaunchedEffect(Unit) {

                    if (userUid == myInfoViewModel.userInfo.value!!.uid) {
                        isMyPostsList = true
                        userId = null
                    }
                    else {

                        if (userId == null) { userUid = null }
                    }
                }

                if (userUid != null) {
                    UserPostsListView(
                        tag = tag,
                        isMyPostsList = isMyPostsList,
                        myInfo = myInfoViewModel.userInfo.value!!,
                        userId = userId,
                        userUid = userUid,
                        mainNavController = mainNavController
                    )
                }
                else {
                    Log.e(tag, "user data not found")
                    RootSnackbar.show("올바르지 않은 접근입니다. 다시 시도해주세요.")
                }
            }
            composable(route = MainNavigationGraph.FAVORITEPOSTSLIST.name){
                val tag = "FAVORITE_POSTS_LIST"

                FavoritePostsListView(
                    tag = tag,
                    myInfo = myInfoViewModel.userInfo.value!!,
                    mainNavController = mainNavController
                )
            }
        }

        PostOptionsView(mainNavController = mainNavController)

        if (isLoading) { LoadingView() }
    }
}