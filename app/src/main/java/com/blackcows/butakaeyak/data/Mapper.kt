package com.blackcows.butakaeyak.data

import com.google.common.reflect.TypeToken
import com.google.gson.Gson

fun <T> T.toMap(): Map<String, Any> {
    val gson = Gson()
    val json = gson.toJson(this)
    return gson.fromJson(json, object: TypeToken<Map<String, Any>>() {}.type)
}