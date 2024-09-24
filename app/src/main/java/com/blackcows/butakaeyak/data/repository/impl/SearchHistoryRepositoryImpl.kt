package com.blackcows.butakaeyak.data.repository.impl

import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.data.models.MedicineDetail
import com.blackcows.butakaeyak.data.source.local.SearchHistoryDataSource
import com.blackcows.butakaeyak.domain.repo.SearchHistoryRepository
import javax.inject.Inject

class SearchHistoryRepositoryImpl @Inject constructor(
    private val searchHistoryDataSource: SearchHistoryDataSource
): SearchHistoryRepository {
    override fun getQueryHistory(): List<String> {
        return searchHistoryDataSource.getQueryHistory()
    }

    override fun saveQueryHistory(query: String) {
        searchHistoryDataSource.saveQueryHistory(query)
    }

    override fun removeQueryHistory() {
        searchHistoryDataSource.removeQueryHistory()
    }

    override fun getMedicineDetailHistory(): List<String> {
        return searchHistoryDataSource.getMedicineDetailHistory()
    }

    override fun saveMedicineDetailHistory(medicine: Medicine) {
        searchHistoryDataSource.saveMedicineDetailHistory(medicine.id!!)
    }

    override fun removeMedicineDetailHistory() {
        searchHistoryDataSource.removeMedicineDetailHistory()
    }
}