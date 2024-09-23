package com.blackcows.butakaeyak.data.source.firebase

import android.util.Log
import com.blackcows.butakaeyak.data.models.Friend
import com.blackcows.butakaeyak.data.models.FriendRequest
import com.blackcows.butakaeyak.data.toMap
import com.blackcows.butakaeyak.data.toObjectWithId
import com.blackcows.butakaeyak.data.toObjectsWithId
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FriendsDataSource @Inject constructor(

) {
    companion object {
        private const val TAG = "FriendsDataSource"
        private const val FRIEND_COLLECTION = "friends"

        private const val PROPOSER = "proposer"
        private const val RECEIVER = "receiver"
        private const val IS_CONNECTED = "isConnected"

        val NOT_REGISTERED = object : Exception() {
            override val message: String
                get() = "등록되지 않은 Friend에 접근을 시도함"
        }
    }

    private val db = Firebase.firestore

    suspend fun propose(userId: String, opponentId: String) {
        val obj = FriendRequest(
            proposer = userId,
            receiver = opponentId,
            isConnected = false
        )

        db.collection(FRIEND_COLLECTION)
            .add(obj)
            .await()
    }

    suspend fun getMyProposes(userId: String): List<Friend> {
        return db.collection(FRIEND_COLLECTION)
            .whereEqualTo(PROPOSER, userId)
            .get().await().toObjectsWithId<Friend>()
    }


    suspend fun getMyReceives(userId: String): List<Friend>
        = db.collection(FRIEND_COLLECTION)
        .whereEqualTo(RECEIVER, userId)
        .get().await().toObjectsWithId<Friend>()

    suspend fun getMyFriends(userId: String): List<Friend> = coroutineScope {
        val propose =  async { getMyProposes(userId) }
        val receive = async { getMyReceives(userId) }

        (receive.await() + propose.await()).filter {
            it.isConnected
        }
    }

    suspend fun cancelFriend(userId: String, opponentId: String) {
        val relationship = getMyFriends(userId).firstOrNull {
            it.isConnected && (it.proposer == opponentId || it.receiver == opponentId)
        }

        if(relationship != null) {
            db.collection(FRIEND_COLLECTION)
                .document(relationship.id)
                .delete()
                .await()
        } else {
            throw NOT_REGISTERED
        }
    }

    suspend fun accept(friend: Friend) {
        val relationship = db.collection(FRIEND_COLLECTION)
            .document(friend.id)
            .get().await().toObjectWithId<Friend>()

        if(relationship != null) {
            val accepted = friend.copy(
                isConnected = true
            )
            db.collection(FRIEND_COLLECTION)
                .document(accepted.id)
                .set(accepted.toMap())
                .await()
        } else {
            throw NOT_REGISTERED
        }
    }
}