package com.blackcows.butakaeyak.data.repository.impl

import android.util.Log
import com.blackcows.butakaeyak.data.models.MyPharmacy
import com.blackcows.butakaeyak.data.source.link.MyPharmacyDataSource
import com.blackcows.butakaeyak.domain.repo.MyPharmacyRepository
import javax.inject.Inject

class MyPharmacyRepositoryImpl @Inject constructor(
    private val myPharmacyDataSource: MyPharmacyDataSource
): MyPharmacyRepository {

    companion object {
        private const val TAG= "MyPharmacyRepositoryImpl"
    }

    override suspend fun getMyFavorites(userId: String): List<MyPharmacy> {
        return runCatching {
            myPharmacyDataSource.getMyPharmacies(userId)
        }.onFailure {
            Log.w(TAG, "getMyFavorites Failed) msg: ${it.message}")
        }.getOrDefault(listOf())
    }

    override suspend fun addToFavorite(myPharmacy: MyPharmacy) {
        runCatching {
            myPharmacyDataSource.addSinglePharmacy(myPharmacy)
        }.onFailure {
            Log.w(TAG, "addToFavorite Failed) msg: ${it.message}")
        }
    }

    override suspend fun cancelFavorite(userId: String, myPharmacy: MyPharmacy) {
        runCatching {
            myPharmacyDataSource.removePharmacy(myPharmacy)
        }.onFailure {
            Log.w(TAG, "cancelFavorite Failed) msg: ${it.message}")
        }
    }

}