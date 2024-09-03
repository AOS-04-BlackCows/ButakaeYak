package com.blackcows.butakaeyak.data.repository

import com.blackcows.butakaeyak.data.models.Drug
import com.blackcows.butakaeyak.data.models.Pill

interface MedicineRepository {
    fun addDrug(drug: Drug, callback: (Boolean) -> Unit)
    fun searchDrugs(name: String, callback: (List<Drug>) -> Unit)

    fun addPill(pill: Pill, callback: (Boolean) -> Unit)
    fun searchPills(name: String, callback: (List<Pill>) -> Unit)
}