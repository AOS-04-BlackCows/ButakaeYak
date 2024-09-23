package com.blackcows.butakaeyak.data.retrofit

import com.blackcows.butakaeyak.BuildConfig

sealed class ApiBaseUrl(val url: String) {
    data object DrugInfoUrl: ApiBaseUrl("http://apis.data.go.kr/1471000/")
    data object KakaoPlaceSearchUrl: ApiBaseUrl("https://dapi.kakao.com/v2/local/search/")
    data object MedicineUrl: ApiBaseUrl("https://${BuildConfig.ALGORIA_APP_ID}.algolia.net")
    data object PharmacyListInfoUrl: ApiBaseUrl("http://apis.data.go.kr/B552657/")
    data object MedicineInfoUrl: ApiBaseUrl("http://apis.data.go.kr/1471000/DrugPrdtPrmsnInfoService06/")
    data object GPTUrl: ApiBaseUrl("https://api.openai.com/v1/chat/")
}