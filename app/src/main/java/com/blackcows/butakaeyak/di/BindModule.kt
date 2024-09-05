package com.blackcows.butakaeyak.di

import com.blackcows.butakaeyak.data.repository.DrugRepository
import com.blackcows.butakaeyak.data.repository.LocalRepository
import com.blackcows.butakaeyak.data.repository.MedicineRepository
import com.blackcows.butakaeyak.domain.repo.KakaoMapRepository
import com.blackcows.butakaeyak.data.repository.impl.DrugRepositoryImpl
import com.blackcows.butakaeyak.data.repository.impl.LocalRepositoryImpl
import com.blackcows.butakaeyak.data.repository.impl.KakaoMapRepositoryImpl
import com.blackcows.butakaeyak.data.repository.impl.MedicineRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
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
    abstract fun provideMedicineRepository(impl: MedicineRepositoryImpl): MedicineRepository

    @Binds
    //@ViewModelScoped
    abstract fun provideKakaoRepository(impl: KakaoMapRepositoryImpl): KakaoMapRepository
}
