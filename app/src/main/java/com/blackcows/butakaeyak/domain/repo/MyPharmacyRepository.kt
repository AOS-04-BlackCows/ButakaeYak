package com.blackcows.butakaeyak.domain.repo

import com.blackcows.butakaeyak.data.models.MyPharmacy

interface MyPharmacyRepository {
    suspend fun getMyFavorites(userId: String): List<MyPharmacy>
    suspend fun addToFavorite(myPharmacy: MyPharmacy)
    suspend fun cancelFavorite(userId: String, myPharmacy: MyPharmacy)
}