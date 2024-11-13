package com.example.tradingapp.model.viewmodel.post

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.tradingapp.model.data.post.PostDetails
import com.example.tradingapp.model.repository.PostDataRepository

class PostForTradingViewModel (
) : ViewModel() {
    val otherTradingPostsList = mutableListOf<PostDetails>()
    val lazyListState by derivedStateOf { LazyListState(0, 0) }


    fun getAlphaValueForHeader(
        firstVisibleItemIndex : Int,
        firstVisibleItemScrollOffset : Int
    ) : Long {
        var offset : Int

        if (firstVisibleItemIndex != 0){
            return (0xFF000000)
        }

        if ((firstVisibleItemScrollOffset / 3) >= 0xFF){
            offset = 0xFF
        }
        else {
            offset = (firstVisibleItemScrollOffset / 3)
        }

        return (offset.toLong() * 0X01000000)
    }


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