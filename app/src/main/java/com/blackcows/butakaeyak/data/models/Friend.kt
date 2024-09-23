package com.blackcows.butakaeyak.data.models

import com.google.gson.annotations.Expose

data class Friend(
    @Expose(serialize = true)
    val id: String,
    val proposer: String,
    val receiver: String,
    val isConnected: Boolean
) {
    fun toRequest()
    = FriendRequest(
        proposer, receiver, isConnected
    )
}

data class FriendRequest(
    val proposer: String,
    val receiver: String,
    val isConnected: Boolean
)