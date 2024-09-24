package com.blackcows.butakaeyak.data.source.local

import com.blackcows.butakaeyak.data.models.MedicineDetail

interface MedicineInfoRepository {
    suspend fun searchByName(query: String): List<MedicineDetail>
}