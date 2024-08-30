package com.example.yactong.data.save_raw

import com.example.yactong.BuildConfig
import com.example.yactong.data.retrofit.ApiBaseUrl
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AiRetrofitClient {

    private const val BASE_URL = "https://api.openai.com/v1/chat/completions/"
    private val key = BuildConfig.OPEN_AI_KEY

    private var instance: Retrofit? = null

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    fun getInstance() : Retrofit {
        if(instance == null) {
            instance = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(getOkhttpClient())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }

        return instance!!
    }

    private fun getOkhttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder()
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val requestBuilder = originalRequest.newBuilder()
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer $key")
                val request = requestBuilder.build()

                chain.proceed(request)
            }
            .build()
    }
}