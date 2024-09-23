package com.blackcows.butakaeyak.domain.result

import com.blackcows.butakaeyak.data.models.User

sealed class LoginResult {
    data class Success(val user: User) : LoginResult()
    data object UnknownAccount : LoginResult()
    data object Failure : LoginResult()
}