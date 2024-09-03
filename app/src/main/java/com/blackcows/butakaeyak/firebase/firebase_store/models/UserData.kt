package com.blackcows.butakaeyak.firebase.firebase_store.models


// 사용자 데이터를 포함하는 객체 생성
data class UserData(
    val name : String,
    val age : Int,
    val phoneNumber : String
) {
    fun toMap() : HashMap<String, String>{
        return hashMapOf(
            "name" to name,
            "age" to age.toString(),
            "phoneNumber" to phoneNumber
        )
    }
}
