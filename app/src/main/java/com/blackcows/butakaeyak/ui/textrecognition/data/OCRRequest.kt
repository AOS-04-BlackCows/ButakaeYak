package com.blackcows.butakaeyak.ui.textrecognition.data

import com.blackcows.butakaeyak.ui.textrecognition.AI_MODEL
import com.google.gson.annotations.SerializedName

data class OCRRequest(
    val model: String = AI_MODEL,
    val messages: List<Message>,
)

data class OCRResponse(
    @SerializedName("choices")
    val choices: List<Choice?>?,
    @SerializedName("created")
    val created: Int?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("model")
    val model: String?,
    @SerializedName("object")
    val objectX: String?
)

data class Message(
    @SerializedName("role")
    val role: String?,
    @SerializedName("content")
    val content: String?
)

data class Choice(
    @SerializedName("finish_reason")
    val finishReason: String?,
    @SerializedName("index")
    val index: Int?,
    @SerializedName("logprobs")
    val logprobs: Any?,
    @SerializedName("message")
    val message: Message?
)