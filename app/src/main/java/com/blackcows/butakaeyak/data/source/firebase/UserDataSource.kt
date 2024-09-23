package com.blackcows.butakaeyak.data.source.firebase

import android.util.Log
import com.blackcows.butakaeyak.data.models.User
import com.blackcows.butakaeyak.data.models.UserRequest
import com.blackcows.butakaeyak.data.toMap
import com.blackcows.butakaeyak.data.toObjectWithId
import com.blackcows.butakaeyak.data.toObjectsWithId
import com.blackcows.butakaeyak.firebase.firebase_store.models.UserData
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserDataSource @Inject constructor(

){
    private val db = Firebase.firestore


    companion object {
        private const val TAG = "UserSource"
        private const val USER_COLLECTION = "users"

        private const val LOGIN_ID = "loginId"
        private const val PASSWORD = "pwd"
        private const val KAKAO_ID = "kakaoId"

        val NOT_REGISTER_USER = object: Exception() {
            override val message: String
                get() = "존재하지 않는 User에 접근 시도함."
        }
    }

    suspend fun getUserWithId(id: String): User? {
        return db.collection(USER_COLLECTION)
            .document(id)
            .get().await().toObjectWithId()
    }

    suspend fun updateUser(user: User): User {
        val saved = db.collection(USER_COLLECTION)
            .document(user.id).get().await()

        if(saved == null) {
            throw NOT_REGISTER_USER
        } else {
            db.collection(USER_COLLECTION)
                .document(user.id)
                .set(user.toRequest()).await()

            return getUserWithId(user.id)!!
        }
    }

    suspend fun getUserWithLoginId(id: String, pwd: String): User? {
        return db.collection(USER_COLLECTION)
            .whereEqualTo(LOGIN_ID, id)
            .whereEqualTo(PASSWORD, pwd)
            .get().await().toObjectsWithId<User>().getOrNull(0)
    }

    suspend fun isDuplicatedId(id: String): Boolean {
        val result = db.collection(USER_COLLECTION)
                .whereEqualTo(LOGIN_ID, id)
                .get().await().toObjectsWithId<User>().getOrNull(0)

        return (result != null)
    }

    suspend fun getUserWithKakaoId(id: Long): User? {
        return db.collection(USER_COLLECTION)
            .whereEqualTo(KAKAO_ID, id)
            .get().await().toObjectsWithId<User>().firstOrNull()
    }

    suspend fun saveUser(user: User): User {
        val userRequest = user.toRequest()
        return db.collection(USER_COLLECTION)
                .add(userRequest)
                .await().get().await().toObjectWithId<User>()!!
    }
    suspend fun saveUser(user: UserRequest): User {
        return db.collection(USER_COLLECTION)
            .add(user)
            .await().get().await().toObjectWithId<User>()!!
    }

    suspend fun registerDeviceToken(user: User, token: String): User? {
        val withToken = user.copy(
            deviceToken = token
        )

        db.collection(USER_COLLECTION)
            .document(user.id)
            .set(withToken)

        return withToken
    }

    suspend fun deleteAccount(user: User) {
        db.collection(USER_COLLECTION)
            .document(user.id)
            .delete().await()
    }
}