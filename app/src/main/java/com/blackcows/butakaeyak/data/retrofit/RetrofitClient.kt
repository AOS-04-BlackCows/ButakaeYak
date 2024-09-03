package com.blackcows.butakaeyak.data.retrofit

import com.blackcows.butakaeyak.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    const val DRUG_REST_API_KEY = BuildConfig.DRUG_INFO_KEY

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
            ApiBaseUrl.DrugInfoUrl -> {
                OkHttpClient().newBuilder()
                    .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
                    .build()
            }
        }
    }

}