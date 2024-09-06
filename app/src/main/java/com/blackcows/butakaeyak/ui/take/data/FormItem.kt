package com.blackcows.butakaeyak.ui.take.data

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FormItem(var aImage : Int, var aName : String):Parcelable

@Parcelize
data class CycleItem(val aImage : Bitmap, val aName : String):Parcelable

data class AlarmItem(
    val timeText: String,      // 시간 텍스트
    val isMonEnabled: Boolean, // 월요일 활성화 여부
    val isTueEnabled: Boolean, // 화요일 활성화 여부
    val isWedEnabled: Boolean, // 수요일 활성화 여부
    val isThuEnabled: Boolean, // 목요일 활성화 여부
    val isFriEnabled: Boolean, // 금요일 활성화 여부
    val isSatEnabled: Boolean, // 토요일 활성화 여부
    val isSunEnabled: Boolean, // 일요일 활성화 여부
    val trueColor: Int,      // 활성화 상태의 텍스트 색상
    val falseColor: Int     // 비활성화 상태의 텍스트 색상
)
