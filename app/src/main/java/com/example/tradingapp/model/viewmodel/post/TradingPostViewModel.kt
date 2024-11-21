package com.example.tradingapp.model.viewmodel.post

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.tradingapp.model.data.post.PostDetails
import com.example.tradingapp.model.repository.PostDataRepository

class TradingPostViewModel (
) : ViewModel() {
    var postId : Int? = null
    var postDetails : PostDetails? = null
    var backStackEntryId = mutableStateOf<String?>(null)
    val isPressedAnyPreviewPost = mutableStateOf(false)
    val otherTradingPostsList = mutableListOf<PostDetails>()


    fun isFavoritedPost(
        tag : String,
        myUid : Int,
        postId : Int,
        callback : (Boolean) -> Unit
    ) {
        PostDataRepository.isFavoritedPost(tag, myUid, postId) { isFavorited ->
            if (isFavorited != null) { callback(isFavorited) }
            else    { callback(false) }
        }
    }


    fun favoritePost(
        tag : String,
        myUid : Int,
        postId : Int,
        callback : (Boolean) -> Unit
    ) {
        PostDataRepository.favoritePost(tag, myUid, postId) { isSuccessful ->
            callback(isSuccessful)
        }
    }


    fun unfavoritePost(
        tag: String,
        myUid: Int,
        postId: Int,
        callback: (Boolean) -> Unit
    ) {
        PostDataRepository.unfavoritePost(tag, myUid, postId) { isSuccessful ->
            callback(isSuccessful)
        }
    }
}