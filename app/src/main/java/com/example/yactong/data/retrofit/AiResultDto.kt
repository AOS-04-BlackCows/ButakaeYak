package com.example.yactong.data.retrofit

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AiResultDto(
    @SerializedName("choices") val choice: List<Choice>
): Parcelable

@Parcelize
data class Choice(
    @SerializedName("message") val msg: Message
): Parcelable

@Parcelize
data class Message(
    @SerializedName("role") val role: String,
    @SerializedName("content") val medicine: List<Medicine>
): Parcelable

