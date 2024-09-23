package com.blackcows.butakaeyak.data.source.link

import com.blackcows.butakaeyak.data.models.MedicineGroup
import com.blackcows.butakaeyak.data.models.MyPharmacy

interface MyPharmacyDataSource {
    suspend fun getMyPharmacies(userId: String): List<MyPharmacy>
    suspend fun saveMyPharmacies(pharmacies: List<MyPharmacy>)
    suspend fun addSinglePharmacy(pharmacy: MyPharmacy)
    suspend fun removePharmacy(pharmacy: MyPharmacy)
}