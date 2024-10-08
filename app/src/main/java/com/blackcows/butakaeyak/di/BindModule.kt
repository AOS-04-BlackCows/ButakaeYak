package com.blackcows.butakaeyak.di

import com.blackcows.butakaeyak.domain.repo.DrugRepository
import com.blackcows.butakaeyak.domain.repo.KakaoMapRepository
import com.blackcows.butakaeyak.domain.repo.LocalRepository
import com.blackcows.butakaeyak.domain.repo.MedicineRepository
import com.blackcows.butakaeyak.data.repository.impl.DrugRepositoryImpl
import com.blackcows.butakaeyak.data.repository.impl.LocalRepositoryImpl
import com.blackcows.butakaeyak.data.repository.impl.KakaoMapRepositoryImpl
import com.blackcows.butakaeyak.data.repository.impl.MedicineRepositoryImpl
import com.blackcows.butakaeyak.data.repository.impl.PharmacyRepositoryImpl
import com.blackcows.butakaeyak.data.repository.impl.UserRepositoryImpl
import com.blackcows.butakaeyak.domain.repo.PharmacyRepository
import com.blackcows.butakaeyak.domain.repo.UserRepository
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

    @Binds
    //@ViewModelScoped
    abstract fun provideUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    //@ViewModelScoped
    abstract fun providePharmacyRepository(impl: PharmacyRepositoryImpl): PharmacyRepository
}
