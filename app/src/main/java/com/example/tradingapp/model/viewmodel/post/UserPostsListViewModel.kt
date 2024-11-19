package com.example.tradingapp.model.viewmodel.post

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.tradingapp.model.data.post.PostDetails

class UserPostsListViewModel : ViewModel() {
    val backStackEntryId = mutableStateOf<String?>(null)
    var startPageNumber = 0
    var startLoadingPostNumber = 0
    val lazyListState = derivedStateOf { LazyListState(0, 0) }
    val tradingPostsList = mutableListOf<PostDetails>()
}