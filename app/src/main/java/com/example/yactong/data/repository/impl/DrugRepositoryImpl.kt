package com.example.yactong.data.repository.impl

import com.example.yactong.data.models.Drug
import com.example.yactong.data.models.Pill
import com.example.yactong.data.repository.DrugRepository
import com.example.yactong.data.source.api.DrugDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class DrugRepositoryImpl @Inject constructor(
    private val drugDataSource: DrugDataSource,
    private val ioDispatcher: CoroutineDispatcher
): DrugRepository {
    override fun searchDrugs(name: String, callback: (List<Drug>) -> Unit) {
        CoroutineScope(ioDispatcher).launch {
            kotlin.runCatching {
                drugDataSource.searchDrugs(name)
            }.onSuccess {
                callback(it)
            }.onFailure { callback(listOf()) }
        }
    }

    override fun searchPills(name: String, callback: (List<Pill>) -> Unit) {
        CoroutineScope(ioDispatcher).launch {
            kotlin.runCatching {
                drugDataSource.searchPills(name)
            }.onSuccess {
                callback(it)
            }.onFailure { callback(listOf()) }
        }
    }
}