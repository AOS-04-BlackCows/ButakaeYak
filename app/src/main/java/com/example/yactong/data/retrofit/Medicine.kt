package com.example.yactong.data.retrofit

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Medicine (
    val id: String? = null,
    val name: String? = null,
    val enterprise: String? = null,
    val effect: String? = null,
    val instructions: String? = null,
    val warning: String? = null,
    val caution: String? = null,
    val interaction: String? = null,
    val sideEffect: String? = null,
    val storingMethod: String? = null,
    val imageUrl: String? = null
): Parcelable