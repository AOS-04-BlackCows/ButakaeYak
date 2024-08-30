package com.example.yactong.data.save_raw

import android.util.Log
import com.example.yactong.data.models.Drug
import com.example.yactong.data.repository.DrugRepository
import javax.inject.Inject

class SaveMedicineModule @Inject constructor(
    private val drugRepository: DrugRepository
) {
    // total items count: 4777
    // max items per page: 100

    private val TAG = "SaveMedicineModule"

    suspend fun doSave() {
        for(i in 1..48) {
            var isSuccess = true
            kotlin.runCatching {
                val lists = getDrugs("", i, 100)
            }.onSuccess {
                isSuccess = true
            }.onFailure {
                Log.e(TAG, "Failed at $i")
                isSuccess = false
            }

            if(!isSuccess) break
        }
    }

    private fun summarize(drug: Drug) {

    }

    private suspend fun getDrugs(name: String, page: Int, itemNum: Int): List<Drug> {
        return drugRepository.searchDrugs(name, page, itemNum)
    }
}