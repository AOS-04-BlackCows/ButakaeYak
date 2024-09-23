package com.blackcows.butakaeyak.domain.repo

import android.graphics.Bitmap
import com.blackcows.butakaeyak.data.models.User
import com.blackcows.butakaeyak.data.models.UserRequest
import com.blackcows.butakaeyak.domain.result.LoginResult
import com.blackcows.butakaeyak.domain.result.SignUpResult

interface UserRepository {
    suspend fun loginWithId(id: String, pwd: String): LoginResult
    suspend fun loginWithKakaoId(kakaoId: Long): LoginResult

    suspend fun signUpUserData(userRequest: UserRequest): SignUpResult
    suspend fun trySignUpWithKakao(): SignUpResult

    suspend fun logout(): Boolean
    suspend fun deleteAccount(user: User)

    suspend fun setProfile(user: User, bitmap: Bitmap): User
    suspend fun setProfile(user: User, url: String): User
    suspend fun deleteProfile(user: User): User

    suspend fun registerDeviceToken(user: User): User
}