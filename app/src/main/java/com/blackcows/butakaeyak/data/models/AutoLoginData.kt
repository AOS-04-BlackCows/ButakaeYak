package com.blackcows.butakaeyak.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AutoLoginData (
    val isKakao: Boolean,
    val loginId: String,
    val pwd: String,
    val kakaoId: String
): Parcelable