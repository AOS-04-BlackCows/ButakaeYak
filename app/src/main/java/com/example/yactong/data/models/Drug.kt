package com.example.yactong.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class Drug(
    val id: String?,
    val name: String?,
    val enterprise: String?,
    val effect: String?,
    val instructions: String?,
    val warning: String?,
    val caution: String?,
    val interaction: String?,
    val sideEffect: String?,
    val storingMethod: String?,
    val openDate: LocalDate,
    val updateDate: LocalDate,
    val imageUrl: String?
): Parcelable
