package com.blackcows.butakaeyak.domain

import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.domain.repo.LocalUtilsRepository
import com.blackcows.butakaeyak.domain.repo.MedicineRepository
import javax.inject.Inject

class GetMedicinesNameUseCase @Inject constructor(
    private val medicineRepository: MedicineRepository,
    private val localUtilsRepository: LocalUtilsRepository
) {
    suspend operator fun invoke(name: String): List<Medicine>
    {
        //TODO 검색 결과 추가
//       localUtilsRepository.saveQuery(name)
        // TODO 검색후 클릭한 약 추가
//       localUtilsRepository.saveViewed(name)
        return medicineRepository.searchMedicinesByName(name)
    }

}