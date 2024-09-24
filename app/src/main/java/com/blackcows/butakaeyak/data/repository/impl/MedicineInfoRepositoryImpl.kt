package com.blackcows.butakaeyak.data.repository.impl

import com.blackcows.butakaeyak.data.models.MedicineDetail
import com.blackcows.butakaeyak.data.source.api.MedicineInfoDataSource
import com.blackcows.butakaeyak.data.source.local.MedicineInfoRepository
import javax.inject.Inject

class MedicineInfoRepositoryImpl @Inject constructor(
    private val medicineInfoDataSource: MedicineInfoDataSource
): MedicineInfoRepository {
    override suspend fun searchByName(query: String): List<MedicineDetail> {
        return medicineInfoDataSource.searchMedicines(name = query)
    }
}