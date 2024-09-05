package com.blackcows.butakaeyak.domain.repo

import com.blackcows.butakaeyak.data.models.KakaoPlace

interface KakaoMapRepository {
    suspend fun searchCategory(x: String,y: String): List<KakaoPlace>
    fun searchPlace(x: String,y: String, callback: (List<KakaoPlace>) -> (Unit))
}