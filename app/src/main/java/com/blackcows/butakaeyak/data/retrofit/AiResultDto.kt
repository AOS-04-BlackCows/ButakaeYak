package com.blackcows.butakaeyak.data.retrofit

import android.os.Parcelable
import com.blackcows.butakaeyak.data.models.Drug
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
    @SerializedName("content") val medicineJson: String
): Parcelable

