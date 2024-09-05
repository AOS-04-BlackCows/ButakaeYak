package com.blackcows.butakaeyak.domain.repo

import com.blackcows.butakaeyak.data.models.Medicine

interface MedicineRepository {
    suspend fun searchMedicinesByName(name: String): List<Medicine>
}