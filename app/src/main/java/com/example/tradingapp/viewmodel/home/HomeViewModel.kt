package com.example.tradingapp.viewmodel.home

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.tradingapp.model.data.post.PostDetails

class HomeViewModel : ViewModel() {
    val homeGraphRootEntryId = mutableStateOf<String?>(null)
    var reloadPostsList = mutableStateOf(false)
    var startPageNumber = 0
    var startLoadingPostNumber = 0
    val lazyListState = LazyListState(0, 0)
    val postsList : MutableList<PostDetails> = mutableListOf()

    fun removeItemByPostId(postId : Int) {
        postsList.forEachIndexed { index, post ->

            if (post.postID == postId) {
                reloadPostsList.value = true
                postsList.removeAt(index)
                if (startLoadingPostNumber > 0) startLoadingPostNumber--
                return
            }
        }
    }
}