package com.blackcows.butakaeyak.data.repository.impl

import android.util.Log
import com.blackcows.butakaeyak.data.models.Friend
import com.blackcows.butakaeyak.data.source.firebase.FriendsDataSource
import com.blackcows.butakaeyak.domain.repo.FriendRepository
import javax.inject.Inject

class FriendRepositoryImpl @Inject constructor(
    private val friendsDataSource: FriendsDataSource
): FriendRepository {

    companion object {
        private const val TAG = "FriendRepositoryImpl"
    }

    override suspend fun getMyProposal(userId: String): List<Friend> {
        return runCatching {
            friendsDataSource.getMyProposes(userId)
        }.getOrDefault(listOf())
    }

    override suspend fun getMyReceived(userId: String): List<Friend> {
        return runCatching {
            friendsDataSource.getMyReceives(userId)
        }.getOrDefault(listOf())
    }

    override suspend fun getMyFriends(userId: String): List<Friend> {
        return runCatching {
            friendsDataSource.getMyFriends(userId)
        }.getOrDefault(listOf())
    }

    override suspend fun requestFriend(userId: String, opponentId: String) {
        runCatching {
            val requestList = getMyProposal(userId) + getMyReceived(opponentId)
            val sameRequest = requestList.filter {
                (it.proposer == userId && it.receiver == opponentId) || (it.proposer == opponentId && it.receiver == userId)
            }

            if(sameRequest.isEmpty()) {
                friendsDataSource.propose(userId, opponentId)
            }
        }.onFailure {
            Log.w(TAG, "requestFriend failed) msg: ${it.message}")
        }
    }

    override suspend fun acceptRequest(friend: Friend) {
        runCatching {
            friendsDataSource.accept(friend)
        }.onFailure {
            Log.w(TAG, "acceptRequest failed) msg: ${it.message}")
        }
    }

    override suspend fun disconnectFriend(userId: String, opponentId: String) {
        runCatching {
            friendsDataSource.cancelFriend(userId, opponentId)
        }.onFailure {
            Log.w(TAG, "disconnectFriend failed) msg: ${it.message}")
        }
    }

}