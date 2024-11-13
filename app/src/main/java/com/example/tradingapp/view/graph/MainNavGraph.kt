package com.example.tradingapp.view.graph

import android.util.Log
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tradingapp.model.data.navigation.MainNavigationGraph
import com.example.tradingapp.model.data.post.PostDetails
import com.example.tradingapp.model.viewmodel.verify.LoginViewModel
import com.example.tradingapp.model.viewmodel.verify.UserInformationViewModel
import com.example.tradingapp.view.post.trading.FavoritePostsListView
import com.example.tradingapp.view.verify.LoginView
import com.example.tradingapp.view.verify.RegisterView
import com.example.tradingapp.view.post.trading.PostForTradingView
import com.example.tradingapp.view.post.trading.UserPostsListView
import com.example.tradingapp.view.post.trading.WritePostForTradingScreen
import com.example.tradingapp.view.profile.SettingOptionsView

@Composable
fun MainNavGraph (
    loginViewModel : LoginViewModel = viewModel(),
    myInfoViewModel : UserInformationViewModel = viewModel(),
    mainNavController : NavHostController = rememberNavController()
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
                selectedPost = { getPostDetails ->
                    mainNavController.currentBackStackEntry?.savedStateHandle?.set("selectedPostDetails", getPostDetails)
                    mainNavController.navigate(MainNavigationGraph.POSTFORTRADING.name)
                },
                navigateFromMain = { route ->

                    if (route == MainNavigationGraph.USERTRADINGPOSTSLIST.name) {
                        mainNavController.currentBackStackEntry?.savedStateHandle?.set("user_uid", myInfoViewModel.userInfo.value!!.uid)
                    }
                    mainNavController.navigate(route)
                }
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

            WritePostForTradingScreen(
                tag = tag,
                myUID = myInfoViewModel.userInfo.value!!.uid,
                mainNavController = mainNavController
            )
        }
        composable(route = MainNavigationGraph.POSTFORTRADING.name){
            val tag = "POST_FOR_TRADING_VIEW"
            val postDetails : PostDetails? = mainNavController
                .previousBackStackEntry?.savedStateHandle?.get("selectedPostDetails")

            if (postDetails != null) {
                PostForTradingView(
                    tag = tag,
                    myUid = myInfoViewModel.userInfo.value!!.uid,
                    postDetails = postDetails,
                    mainNavController = mainNavController,
                    getUserInfo = { getUserUid, getUserId ->
                        Log.d(tag, "get user uid : $getUserUid , user id : $getUserId")
                        mainNavController.currentBackStackEntry?.savedStateHandle?.set("user_uid", getUserUid)
                        mainNavController.currentBackStackEntry?.savedStateHandle?.set("user_id", getUserId)
                        mainNavController.navigate(MainNavigationGraph.USERTRADINGPOSTSLIST.name)
                    },
                    selectedPostDetails = { getPostDetails ->
                        mainNavController.currentBackStackEntry?.savedStateHandle?.set("selectedPostDetails", getPostDetails)
                        mainNavController.navigate(MainNavigationGraph.POSTFORTRADING.name)
                    }
                )
            }
            else {

                if (mainNavController.currentBackStackEntry?.destination?.route == MainNavigationGraph.POSTFORTRADING.name){
                    mainNavController.popBackStack()
                }
                Log.e(tag, "post data not found")
            }
        }
        composable(route = MainNavigationGraph.USERTRADINGPOSTSLIST.name){
            val tag = "USER_TRADING_POSTS_LIST_VIEW"
            var userId : String? = mainNavController.previousBackStackEntry?.savedStateHandle?.get("user_id")
            var userUid : Int? = mainNavController.previousBackStackEntry?.savedStateHandle?.get("user_uid")

            Log.d(tag, "user uid : $userUid , user id : $userId")

            if (userUid == myInfoViewModel.userInfo.value!!.uid) {
                userId = null
            }
            else {
                if (userId == null) { userUid = null }
            }

            if (userUid != null) {
                UserPostsListView(
                    tag = tag,
                    userId = userId,
                    userUid = userUid,
                    mainNavController = mainNavController
                ){ getPostDetails ->
                    mainNavController.currentBackStackEntry?.savedStateHandle?.set("selectedPostDetails", getPostDetails)
                    mainNavController.navigate(MainNavigationGraph.POSTFORTRADING.name)
                }
            }
            else {
                if (mainNavController.currentBackStackEntry?.destination?.route == MainNavigationGraph.USERTRADINGPOSTSLIST.name){
                    mainNavController.popBackStack()
                }
                Log.e(tag, "user data not found")
            }
        }
        composable(route = MainNavigationGraph.FAVORITEPOSTSLIST.name){
            val tag = "FAVORITE_POSTS_LIST"

            FavoritePostsListView(
                tag = tag,
                myUid = myInfoViewModel.userInfo.value!!.uid,
                mainNavController = mainNavController,
                selectedPostDetails = { getPostDetails ->
                    mainNavController.currentBackStackEntry?.savedStateHandle?.set("selectedPostDetails", getPostDetails)
                    mainNavController.navigate(MainNavigationGraph.POSTFORTRADING.name)
                }
            )
        }
    }
}