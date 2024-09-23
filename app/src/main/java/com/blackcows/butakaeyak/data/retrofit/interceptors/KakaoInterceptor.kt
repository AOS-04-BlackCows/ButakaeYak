package com.blackcows.butakaeyak.data.retrofit.interceptors

import com.blackcows.butakaeyak.BuildConfig
import okhttp3.Interceptor

object KakaoInterceptor {
    fun getInterceptor() =
        Interceptor { chain ->
            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder()
                .header("Authorization", "KakaoAK ${BuildConfig.REST_API_KEY}")
            val request = requestBuilder.build()
            chain.proceed(request)
        }
}