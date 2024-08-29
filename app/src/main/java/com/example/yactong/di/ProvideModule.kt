package com.example.yactong.di

import com.example.yactong.data.repository.DrugRepository
import com.example.yactong.data.repository.impl.DrugRepositoryImpl
import com.example.yactong.data.retrofit.ApiBaseUrl
import com.example.yactong.data.retrofit.DrugApiService
import com.example.yactong.data.retrofit.RetrofitClient
import com.example.yactong.data.source.api.DrugDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import org.jetbrains.annotations.TestOnly

@Module
@InstallIn(SingletonComponent::class)
class ProvideModule {
    @Provides
    //@ViewModelScoped
    fun provideRetrofitService() : DrugApiService {
        return RetrofitClient.getInstance(ApiBaseUrl.DrugInfoUrl).create(DrugApiService::class.java)
    }
}