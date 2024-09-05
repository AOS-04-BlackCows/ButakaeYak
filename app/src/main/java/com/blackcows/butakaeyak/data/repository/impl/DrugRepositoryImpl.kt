package com.blackcows.butakaeyak.data.repository.impl

import com.blackcows.butakaeyak.data.models.Drug
import com.blackcows.butakaeyak.data.models.Pill
import com.blackcows.butakaeyak.domain.repo.DrugRepository
import com.blackcows.butakaeyak.data.source.api.DrugDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class DrugRepositoryImpl @Inject constructor(
    private val drugDataSource: DrugDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
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