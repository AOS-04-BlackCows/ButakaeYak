package com.blackcows.butakaeyak.di

import com.algolia.search.saas.Client
import com.blackcows.butakaeyak.BuildConfig
import com.blackcows.butakaeyak.data.repository.impl.MedicineGroupRepositoryImpl
import com.blackcows.butakaeyak.data.repository.impl.MemoRepositoryImpl
import com.blackcows.butakaeyak.data.repository.impl.MyPharmacyRepositoryImpl
import com.blackcows.butakaeyak.data.retrofit.ApiBaseUrl
import com.blackcows.butakaeyak.data.retrofit.service.DrugApiService
import com.blackcows.butakaeyak.data.retrofit.service.KakaoApiService
import com.blackcows.butakaeyak.data.retrofit.RetrofitClient
import com.blackcows.butakaeyak.data.retrofit.service.MedicineInfoService
import com.blackcows.butakaeyak.ui.textrecognition.OCRTextRecApiService
import com.blackcows.butakaeyak.data.source.api.MedicineInfoDataSource
import com.blackcows.butakaeyak.data.source.firebase.MemoDataSource
import com.blackcows.butakaeyak.data.source.firebase.RemoteMedicineGroupDataSource
import com.blackcows.butakaeyak.data.source.firebase.RemoteMyPharmacyDataSource
import com.blackcows.butakaeyak.data.source.local.LocalMedicineGroupDataSource
import com.blackcows.butakaeyak.data.source.local.LocalMyPharmacyDataSource
import com.blackcows.butakaeyak.data.source.local.LocalUtilsDataSource
import com.blackcows.butakaeyak.domain.repo.MedicineGroupRepository
import com.blackcows.butakaeyak.domain.repo.MemoRepository
import com.blackcows.butakaeyak.domain.repo.MyPharmacyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

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
    fun provideIoDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Provides
    //@ViewModelScoped
    fun provideAlgoliaClient(): Client {
        return Client(BuildConfig.ALGORIA_APP_ID, BuildConfig.ALGORIA_SEARCH_KEY)
    }
    
    @Provides
    //@ViewModelScoped
    fun provideKakaoApiService() : KakaoApiService {
        return RetrofitClient.getInstance(ApiBaseUrl.KakaoPlaceSearchUrl).create(KakaoApiService::class.java)
    }

    @Provides
    //@ViewModelScoped
    fun provideMedicineInfoApiService() : MedicineInfoService {
        return RetrofitClient.getInstance(ApiBaseUrl.MedicineInfoUrl).create(MedicineInfoService::class.java)
    }

    @Provides
    //@ViewModelScoped
    fun provideOCR_TextResApiService() : OCRTextRecApiService {
        return RetrofitClient.getInstance(ApiBaseUrl.GPTUrl).create(OCRTextRecApiService::class.java)
    }

    @Provides
    fun provideMyPharmacyRepository(
        localUtilsDataSource: LocalUtilsDataSource,
        remoteMyPharmacyDataSource: RemoteMyPharmacyDataSource,
        localMyPharmacyDataSource: LocalMyPharmacyDataSource
    ): MyPharmacyRepository {
        return if(localUtilsDataSource.isSignIn()) {
            MyPharmacyRepositoryImpl(remoteMyPharmacyDataSource)
        } else {
            MyPharmacyRepositoryImpl(localMyPharmacyDataSource)
        }

        //TODO: 테스트 후 고치기
        //return MyPharmacyRepositoryImpl(remoteMyPharmacyDataSource)
    }

    @Provides
    fun provideMedicineGroupRepository(
        localUtilsDataSource: LocalUtilsDataSource,
        remoteMedicineGroupDataSource: RemoteMedicineGroupDataSource,
        localMedicineGroupDataSource: LocalMedicineGroupDataSource,
        medicineInfoDataSource: MedicineInfoDataSource,
        memoDataSource: MemoDataSource
    ): MedicineGroupRepository {
        return if(localUtilsDataSource.isSignIn()) {
            MedicineGroupRepositoryImpl(
                medicineGroupDataSource = remoteMedicineGroupDataSource,
                medicineDetailDataSource = medicineInfoDataSource,
                memoDataSource = memoDataSource
            )
        } else {
            MedicineGroupRepositoryImpl(
                medicineGroupDataSource = localMedicineGroupDataSource,
                medicineDetailDataSource = medicineInfoDataSource,
                memoDataSource = memoDataSource
            )
        }
        //TODO: 테스트 후 고치기
//        return MedicineGroupRepositoryImpl(
//                medicineGroupDataSource = remoteMedicineGroupDataSource,
//                medicineDetailDataSource = medicineInfoDataSource,
//                memoDataSource = memoDataSource
//            )
    }

    @Provides
    //@ViewModelScoped
    fun provideMemoRepository(
        remoteMedicineGroupDataSource: RemoteMedicineGroupDataSource,
        medicineInfoDataSource: MedicineInfoDataSource,
        memoDataSource: MemoDataSource
    ): MemoRepository {
        return MemoRepositoryImpl(
            memoDataSource = memoDataSource,
            medicineGroupDataSource = remoteMedicineGroupDataSource,
            medicineInfoDataSource = medicineInfoDataSource
        )
    }
}