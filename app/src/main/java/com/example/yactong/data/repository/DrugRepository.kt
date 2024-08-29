package com.example.yactong.data.repository

import com.example.yactong.data.models.Drug
import com.example.yactong.data.models.Pill

interface DrugRepository {
    suspend fun searchDrugs(name: String): Result<List<Drug>>
    suspend fun searchPills(name: String): Result<List<Pill>>
}