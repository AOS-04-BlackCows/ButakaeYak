package com.blackcows.butakaeyak.domain

import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.domain.repo.MedicineRepository
import javax.inject.Inject

class GetMedicinesNameUseCase @Inject constructor(
    private val medicineRepository: MedicineRepository
){
    suspend operator fun invoke(name: String): List<Medicine>
        = medicineRepository.searchMedicinesByName(name)

}