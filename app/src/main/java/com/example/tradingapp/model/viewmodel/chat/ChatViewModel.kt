package com.example.tradingapp.model.viewmodel.chat

import androidx.lifecycle.ViewModel
import com.example.tradingapp.model.data.chat.Chat

class ChatViewModel : ViewModel() {
    fun GetPreviewGroupChatList() : List<Chat> {
        val groupChatList : MutableList<Chat> = mutableListOf()

        for (idx : Int in 1..10){
            groupChatList.add(
                Chat(
                    id = idx,
                    title = "test${idx}test${idx}test${idx}test${idx}test${idx}test${idx}test${idx}",
                    isPrivate = false,
                    members = null,
                    maxParticipant = 0,
                    messages = null
                )
            )
        }

        return groupChatList.toList()
    }
}