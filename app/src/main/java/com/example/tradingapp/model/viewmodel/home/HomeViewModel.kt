package com.example.tradingapp.model.viewmodel.home

import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.ViewModel
import com.example.tradingapp.model.data.post.PostDetails

class HomeViewModel : ViewModel() {
    var startPageNumber = 0
    var startLoadingPostNumber = 0
    val lazyListState = LazyListState(0, 0)
    val postsList : MutableList<PostDetails> = mutableListOf()
}