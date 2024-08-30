package com.example.yactong.data.save_raw

import com.example.yactong.data.models.Drug
import com.example.yactong.data.repository.DrugRepository
import javax.inject.Inject

class SaveMedicineModule @Inject constructor(
    private val drugRepository: DrugRepository
) {
    // total items count: 4777
    // max items per page: 100

    suspend fun doSave() {
        for(i in 1..48) {
            val lists = getDrugs("", i, 100)
        }
    }

    private suspend fun getDrugs(name: String, page: Int, itemNum: Int): List<Drug> {
        return drugRepository.searchDrugs(name, page, itemNum)
    }
}