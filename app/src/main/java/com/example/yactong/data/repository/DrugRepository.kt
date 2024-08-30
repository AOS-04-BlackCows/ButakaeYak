package com.example.yactong.data.repository

import com.example.yactong.data.models.Drug
import com.example.yactong.data.models.Pill

interface DrugRepository {
    fun searchDrugs(name: String, callback: (List<Drug>) -> (Unit))

    // 사용X: For Saving Api Data on Firebase.
    suspend fun searchDrugs(name: String, page: Int, itemNum: Int): List<Drug>


    fun searchPills(name: String, callback: (List<Pill>) -> (Unit))
}