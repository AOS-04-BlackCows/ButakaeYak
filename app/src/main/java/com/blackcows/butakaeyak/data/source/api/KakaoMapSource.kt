package com.blackcows.butakaeyak.data.source.api

import com.blackcows.butakaeyak.data.models.KakaoPlacePharmacy
import com.blackcows.butakaeyak.data.retrofit.KakaoApiService
import javax.inject.Inject

class KakaoMapSource @Inject constructor(
    private val retrofit: KakaoApiService
) {
    private val tag = "DrugDataSource"

    suspend fun searchCategoryPlace(x: String,y: String): List<KakaoPlacePharmacy> {
        val list = mutableListOf<KakaoPlacePharmacy>()
        val result = retrofit.getCategoryInfo(x, y)
        result.documents.forEach {
            list.add(it.toKakaoPlace())
        }
        return list
    }
}