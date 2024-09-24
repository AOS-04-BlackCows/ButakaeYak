package com.blackcows.butakaeyak.domain.repo

import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.data.models.MedicineDetail

interface SearchHistoryRepository {
    fun getQueryHistory(): List<String>
    fun saveQueryHistory(query: String)
    fun removeQueryHistory()

    fun getMedicineDetailHistory(): List<String>
    fun saveMedicineDetailHistory(medicine: Medicine)
    fun removeMedicineDetailHistory()
}