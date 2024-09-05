package com.blackcows.butakaeyak.domain

import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.domain.repo.MedicineRepository
import javax.inject.Inject

class GetMedicinesNameUseCase @Inject constructor(
    private val medicineRepository: MedicineRepository
){
    operator fun invoke(name: String, callback: (List<Medicine>) -> Unit) {
        medicineRepository.searchMedicinesByName(name, callback)
    }
}