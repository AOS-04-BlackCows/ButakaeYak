package com.blackcows.butakaeyak.data.retrofit

import com.blackcows.butakaeyak.BuildConfig
import com.blackcows.butakaeyak.data.retrofit.interceptors.KakaoInterceptor
import com.blackcows.butakaeyak.data.retrofit.interceptors.MedicineInterceptor
import com.google.gson.GsonBuilder
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val retrofitInstances = mutableMapOf<ApiBaseUrl, Retrofit>()

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    fun getInstance(baseUrl: ApiBaseUrl) : Retrofit {
        return retrofitInstances[baseUrl] ?: createRetrofitInstance(baseUrl).also {
            retrofitInstances[baseUrl] = it
        }
    }

    private fun createRetrofitInstance(baseUrl: ApiBaseUrl): Retrofit {
        val okHttpClient=  getOkhttpClient(baseUrl)

        return Retrofit.Builder()
            .baseUrl(baseUrl.url)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }



    private fun getOkhttpClient(apiBaseUrl: ApiBaseUrl): OkHttpClient {
        return when(apiBaseUrl) {
            ApiBaseUrl.KakaoPlaceSearchUrl -> {
                OkHttpClient().newBuilder().addInterceptor(KakaoInterceptor.getInterceptor()).build()
            }
            ApiBaseUrl.MedicineUrl -> {
                OkHttpClient().newBuilder().addInterceptor(MedicineInterceptor.get()).build()
            }
            ApiBaseUrl.PharmacyListInfoUrl -> {
                OkHttpClient().newBuilder().build()
            }
            ApiBaseUrl.MedicineInfoUrl -> {
                OkHttpClient().newBuilder()
                    .addInterceptor(
                        HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.BODY
                        }
                    )
                    .build()
            }
            ApiBaseUrl.GPTUrl->{
                OkHttpClient().newBuilder().build()
            }
            else -> {
                OkHttpClient().newBuilder().build()
            }
        }
    }

}