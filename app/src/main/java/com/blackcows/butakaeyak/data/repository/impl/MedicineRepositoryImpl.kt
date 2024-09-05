package com.blackcows.butakaeyak.data.repository.impl

import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.domain.repo.MedicineRepository
import com.blackcows.butakaeyak.data.source.firebase.MedicineDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MedicineRepositoryImpl @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val medicineDataSource: MedicineDataSource
): MedicineRepository {

    override suspend fun searchMedicinesByName(name: String): List<Medicine> = runCatching {
        withContext(dispatcher) {
            medicineDataSource.searchMedicinesByName(name)
        }
    }.getOrElse { listOf() }
}