package com.example.yactong.data.retrofit

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Medicine (
    val id: String,
    val name: String,
    val enterprise: String,
    val effect: String,
    val instructions: String,
    val warning: String,
    val caution: String,
    val interaction: String,
    val sideEffect: String,
    val storingMethod: String,
    val imageUrl: String
): Parcelable