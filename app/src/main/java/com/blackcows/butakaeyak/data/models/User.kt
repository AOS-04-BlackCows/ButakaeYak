package com.blackcows.butakaeyak.data.models

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    @Expose(serialize = false)
    val id: String,
    val name: String,
    val loginId: String?,
    val pwd: String?,
    val profileUrl: String?,
    val kakaoId: Long?,
    val naverId: String?,
    val googleId: String?,
    val deviceToken: String?
): Parcelable {
    fun toRequest() = UserRequest(
        name = name,
        loginId = loginId,
        pwd = pwd,
        profileUrl = profileUrl,
        kakaoId = kakaoId,
        naverId = naverId,
        googleId = googleId,
        deviceToken = deviceToken
    )
}


data class UserRequest(
    val name: String,
    val loginId: String? = null,
    val pwd: String? = null,
    val profileUrl: String? = null,
    val kakaoId: Long? = null,
    val naverId: String? = null,
    val googleId: String? = null,
    val deviceToken: String? = null
)
