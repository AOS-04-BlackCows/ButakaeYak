package com.blackcows.butakaeyak.data.repository.impl

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class PharmacyInfoRepositoryImpl @Inject constructor(
    private val pharmacyInfoDataSource: PharmacyInfoDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): PharmacyInfoRepository {
    override fun searchPharmacyInfo(name: String, callback: (List<PharmacyInfo>) -> Unit) {
        CoroutineScope(ioDispatcher).launch {
            kotlin.runCatching {
                pharmacyInfoDataSource.searchPharmacy("경기도 성남시", null, null, null, null)
            }.onSuccess {
                Log.d("searchPharmacyInfo", "searchPharmacyInfo onSuccess")
                callback(it)
            }.onFailure {
                Log.d("searchPharmacyInfo", "searchPharmacyInfo onFailure")
                callback(listOf())
            }
        }
    }
}