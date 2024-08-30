package com.example.yactong.data.save_raw

import com.example.yactong.BuildConfig
import com.example.yactong.data.retrofit.ApiBaseUrl
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object AiRetrofitClient {

    private const val BASE_URL = "https://api.openai.com/v1/chat/"
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

        val timeOut = 10L

        return OkHttpClient().newBuilder()
            .connectTimeout(timeOut, TimeUnit.MINUTES) // 연결 타임아웃: 30초
            .writeTimeout(timeOut, TimeUnit.MINUTES)   // 쓰기 타임아웃: 30초
            .readTimeout(timeOut, TimeUnit.MINUTES)    // 읽기 타임아웃: 30초
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val requestBuilder = originalRequest.newBuilder()
                    .header("Authorization", "Bearer $key")
                val request = requestBuilder.build()

                chain.proceed(request)
            }
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }
}