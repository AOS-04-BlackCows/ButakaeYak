package com.blackcows.butakaeyak.domain.repo

import com.blackcows.butakaeyak.data.models.Medicine

interface MedicineRepository {
    fun searchMedicinesByName(name: String, callback: (List<Medicine>) -> Unit)
}