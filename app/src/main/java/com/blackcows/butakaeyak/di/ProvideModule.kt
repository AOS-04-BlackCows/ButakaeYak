package com.blackcows.butakaeyak.di

import com.algolia.search.saas.Client
import com.blackcows.butakaeyak.BuildConfig
import com.blackcows.butakaeyak.data.repository.DrugRepository
import com.blackcows.butakaeyak.data.repository.impl.DrugRepositoryImpl
import com.blackcows.butakaeyak.data.retrofit.ApiBaseUrl
import com.blackcows.butakaeyak.data.retrofit.DrugApiService
import com.blackcows.butakaeyak.data.retrofit.KakaoApiService
import com.blackcows.butakaeyak.data.retrofit.RetrofitClient
import com.blackcows.butakaeyak.data.source.api.DrugDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.jetbrains.annotations.TestOnly

@Module
@InstallIn(SingletonComponent::class)
class ProvideModule {
    @Provides
    //@ViewModelScoped
    fun provideRetrofitService() : DrugApiService {
        return RetrofitClient.getInstance(ApiBaseUrl.DrugInfoUrl).create(DrugApiService::class.java)
    }

    @Provides
    //@ViewModelScoped
    fun provideKakaoApiService() : KakaoApiService {
        return RetrofitClient.getInstance(ApiBaseUrl.KakaoPlaceSearchUrl).create(KakaoApiService::class.java)
    }

    @Provides
    //@ViewModelScoped
    fun provideIoDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Provides
    //@ViewModelScoped
    fun provideAlgoliaClient(): Client {
        return Client(BuildConfig.ALGORIA_APP_ID, BuildConfig.ALGORIA_SEARCH_KEY)
    }
}