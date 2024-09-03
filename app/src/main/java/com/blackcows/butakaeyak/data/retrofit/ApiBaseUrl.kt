package com.blackcows.butakaeyak.data.retrofit

sealed class ApiBaseUrl(val url: String) {
    data object DrugInfoUrl: ApiBaseUrl("http://apis.data.go.kr/1471000/")
}