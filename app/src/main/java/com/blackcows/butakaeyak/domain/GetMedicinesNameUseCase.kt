package com.blackcows.butakaeyak.domain

import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.domain.repo.LocalUtilsRepository
import com.blackcows.butakaeyak.domain.repo.MedicineRepository
import com.blackcows.butakaeyak.domain.repo.SearchHistoryRepository
import javax.inject.Inject

class GetMedicinesNameUseCase @Inject constructor(
    private val medicineRepository: MedicineRepository,
    private val searchHistoryRepository: SearchHistoryRepository
) {
    suspend operator fun invoke(name: String): List<Medicine>
    {
        val result = medicineRepository.searchMedicinesByName(name)
        searchHistoryRepository.saveQueryHistory(name)
        return result
    }
}