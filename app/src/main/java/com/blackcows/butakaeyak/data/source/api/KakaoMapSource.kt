package com.blackcows.butakaeyak.data.source.api

import com.blackcows.butakaeyak.data.models.KakaoPlacePharmacy
import com.blackcows.butakaeyak.data.retrofit.service.KakaoApiService
import javax.inject.Inject

class KakaoMapSource @Inject constructor(
    private val retrofit: KakaoApiService
) {
    private val tag = "DrugDataSource"

    suspend fun searchCategoryPlace(x: String,y: String, date: Int): List<KakaoPlacePharmacy> {
        val list = mutableListOf<KakaoPlacePharmacy>()
        val result = retrofit.getCategoryInfo(x, y, date)
        result.documents.forEach {
            list.add(it.toKakaoPlace())
        }
        return list
    }
}