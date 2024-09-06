package com.blackcows.butakaeyak.domain.repo

import com.blackcows.butakaeyak.data.models.KakaoPlacePharmacy

interface KakaoMapRepository {
    suspend fun searchCategory(x: String,y: String): List<KakaoPlacePharmacy>
    fun searchPlace(x: String,y: String, callback: (List<KakaoPlacePharmacy>) -> (Unit))
}