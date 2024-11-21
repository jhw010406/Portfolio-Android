package com.example.tradingapp.viewmodel.post

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class FavoritePostsListViewModel : ViewModel() {
    var backStackEntryId = mutableStateOf<String?>(null)
}