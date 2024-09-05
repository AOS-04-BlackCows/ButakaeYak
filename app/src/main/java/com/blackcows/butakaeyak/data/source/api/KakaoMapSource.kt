package com.blackcows.butakaeyak.data.source.api

import com.blackcows.butakaeyak.data.models.KakaoPlace
import com.blackcows.butakaeyak.data.retrofit.KakaoApiService
import javax.inject.Inject

class KakaoMapSource @Inject constructor(
    private val retrofit: KakaoApiService
) {
    private val tag = "DrugDataSource"

    suspend fun searchCategoryPlace(x: String,y: String): List<KakaoPlace> {
        val list = mutableListOf<KakaoPlace>()
        val result = retrofit.getCategoryInfo(x, y)
        result.documents.forEach {
            list.add(it.toKakaoPlace())
        }
        return list
    }
}