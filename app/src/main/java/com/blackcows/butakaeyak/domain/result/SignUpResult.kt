package com.blackcows.butakaeyak.domain.result

import com.blackcows.butakaeyak.data.models.User

sealed class SignUpResult {
    data class Success(val user: User) : SignUpResult()
    data object LoginIdDuplicate : SignUpResult()
    data object  KakaoSignUpFail: SignUpResult()
    data object Failure : SignUpResult()
}