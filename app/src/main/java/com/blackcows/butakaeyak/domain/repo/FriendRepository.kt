package com.blackcows.butakaeyak.domain.repo

import com.blackcows.butakaeyak.data.models.Friend

interface FriendRepository {
    suspend fun getMyProposal(userId: String): List<Friend>
    suspend fun getMyReceived(userId: String): List<Friend>
    suspend fun getMyFriends(userId: String): List<Friend>

    suspend fun requestFriend(userId: String, opponentId: String)
    suspend fun acceptRequest(friend: Friend)
    suspend fun disconnectFriend(userId: String, opponentId: String)
}