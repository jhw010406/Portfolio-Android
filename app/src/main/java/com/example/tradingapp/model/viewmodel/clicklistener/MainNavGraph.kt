package com.example.tradingapp.model.viewmodel.clicklistener

import androidx.navigation.NavHostController

object MainNavGraphClickListener{

    fun navigate(
        fromBackStackEntry: String,
        targetRoute : String,
        mainNavController: NavHostController
    ) {
        mainNavController.currentBackStackEntry?.id.let {

            if (it.equals(fromBackStackEntry)) {
                mainNavController.navigate(targetRoute)
            }
        }
    }
}