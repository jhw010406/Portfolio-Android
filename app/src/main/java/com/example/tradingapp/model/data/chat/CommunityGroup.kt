package com.example.tradingapp.model.data.chat

import com.example.tradingapp.model.data.Address

data class CommunityGroup(
    val title : String,
    val purpose : String,
    val groupIntroduction : String,
    val location : Address,
    // List of the All of the account's ID
    val subscriberList : List<String>,
    // account id <-> its level
    // level : moderator, common
    val membersLevel : Map<String, String>
)
