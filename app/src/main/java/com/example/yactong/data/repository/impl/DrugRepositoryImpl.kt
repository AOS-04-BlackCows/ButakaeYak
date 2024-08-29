package com.example.yactong.data.repository.impl

import com.example.yactong.data.models.Drug
import com.example.yactong.data.models.Pill
import com.example.yactong.data.repository.DrugRepository
import com.example.yactong.data.source.api.DrugDataSource
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class DrugRepositoryImpl @Inject constructor(
    private val drugDataSource: DrugDataSource
): DrugRepository {
    override suspend fun searchDrugs(name: String): Result<List<Drug>>
    = kotlin.runCatching {
        drugDataSource.searchDrugs(name)
    }

    override suspend fun searchPills(name: String): Result<List<Pill>>
    = kotlin.runCatching {
        drugDataSource.searchPills(name)
    }
}