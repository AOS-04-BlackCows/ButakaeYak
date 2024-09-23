package com.blackcows.butakaeyak.data

import com.google.common.reflect.TypeToken
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.Gson
import com.google.gson.GsonBuilder

fun <T> T.toMap(): Map<String, Any> {
    val gson = GsonBuilder()
        .addSerializationExclusionStrategy(object: ExclusionStrategy {
            override fun shouldSkipField(f: FieldAttributes?): Boolean {
                return f?.name!!.lowercase() == "id"
            }

            override fun shouldSkipClass(clazz: Class<*>?): Boolean {
                return false
            }
        })
        .create()

    val json = gson.toJson(this)
    return gson.fromJson(json, object: TypeToken<Map<String, Any>>() {}.type)
}

// 모든 데이터 클래스에 대해 toMap을 사용하여 Firestore 문서의 ID를 포함시키는 함수
inline fun <reified T : Any> DocumentSnapshot.toObjectWithId(): T? {
    val gson = Gson()

    // 문서 ID를 포함한 데이터를 맵으로 변환
    val map = this.data?.toMutableMap() ?: mutableMapOf()
    map["id"] = this.id

    // Gson을 사용하여 맵을 객체로 변환
    return gson.fromJson(gson.toJson(map), object : TypeToken<T>() {}.type)
}

inline fun <reified T : Any> QuerySnapshot.toObjectsWithId(): List<T> {
    return this.documents.mapNotNull {
        it.toObjectWithId()
    }
}
