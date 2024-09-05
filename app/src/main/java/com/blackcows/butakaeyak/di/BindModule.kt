package com.blackcows.butakaeyak.di

import com.blackcows.butakaeyak.data.repository.DrugRepository
import com.blackcows.butakaeyak.data.repository.KakaoMapRepository
import com.blackcows.butakaeyak.data.repository.LocalRepository
import com.blackcows.butakaeyak.data.repository.impl.DrugRepositoryImpl
import com.blackcows.butakaeyak.data.repository.impl.LocalRepositoryImpl
import com.blackcows.butakaeyak.data.repository.impl.KakaoMapRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class BindModule {
    @Binds
    //@ViewModelScoped
    abstract fun provideDrugRepository(impl: DrugRepositoryImpl): DrugRepository

    @Binds
    //@ViewModelScoped
    abstract fun provideLocalRepository(impl: LocalRepositoryImpl): LocalRepository

    @Binds
    //@ViewModelScoped
    abstract fun provideKakaoRepository(impl: KakaoMapRepositoryImpl): KakaoMapRepository
}
