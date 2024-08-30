package com.example.yactong.data.save_raw

import com.example.yactong.data.repository.DrugRepository
import javax.inject.Inject

class SaveMedicineModule @Inject constructor(
    private val drugRepository: DrugRepository
) {
    // total items count: 4777
    // max items per page: 100

    fun doSave() {

    }

    private fun getDrugs() {

    }
}