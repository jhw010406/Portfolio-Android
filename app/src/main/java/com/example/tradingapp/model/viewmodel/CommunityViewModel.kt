package com.example.tradingapp.model.viewmodel

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import com.example.tradingapp.model.data.Chat
import com.example.tradingapp.model.data.OthersForCommunity
import com.example.tradingapp.model.data.Post
import com.example.tradingapp.model.data.PostSubject

class CommunityViewModel : ViewModel(){
    fun GetCommunityPostList() : List<Post>{
        val communityPostList : MutableList<Post> = mutableListOf()

        for(idx : Int in 1..20){
            communityPostList.add(
                Post(
                    id = idx,
                    poster = "test",
                    title = "title ${idx}",
                    content = "content content content content content content content content content content ",
                    postTime = "test",
                    location = null,
                    viewCount = 0,
                    isForTrading = false,
                    othersForTrading = null,
                    othersForCommunity = OthersForCommunity(
                        subject = PostSubject.others.subjects[0],
                        isTrendingPost = true,
                        comments = null
                    )
                )
            )
        }

        return (communityPostList.toList())
    }
}