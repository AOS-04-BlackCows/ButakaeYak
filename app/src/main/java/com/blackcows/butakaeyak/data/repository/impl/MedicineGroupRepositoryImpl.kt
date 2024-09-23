package com.blackcows.butakaeyak.data.repository.impl

import android.util.Log
import com.blackcows.butakaeyak.data.WeekDayUtils
import com.blackcows.butakaeyak.data.models.MedicineGroup
import com.blackcows.butakaeyak.data.source.api.MedicineInfoDataSource
import com.blackcows.butakaeyak.data.source.firebase.MemoDataSource
import com.blackcows.butakaeyak.data.source.link.MedicineGroupDataSource
import com.blackcows.butakaeyak.domain.repo.MedicineGroupRepository
import java.time.LocalDate
import javax.inject.Inject

class MedicineGroupRepositoryImpl @Inject constructor(
    private val medicineGroupDataSource: MedicineGroupDataSource,
    private val medicineDetailDataSource: MedicineInfoDataSource,
    private val memoDataSource: MemoDataSource
): MedicineGroupRepository {

    companion object {
        private const val TAG = "MedicineGroupRepositoryImpl"
    }

    override suspend fun getMyGroups(userId: String): List<MedicineGroup> {
        return kotlin.runCatching {
            medicineGroupDataSource.getMedicineGroups(userId).map { group ->
                val medicines = group.medicineIdList?.map {
                    medicineDetailDataSource.searchMedicinesWithId(it)[0]
                }
                val daysWeek = group.daysOfWeeks?.map {
                    WeekDayUtils.fromKorean(it)
                }

                MedicineGroup(
                    id = group.id!!,
                    name = group.name!!,
                    userId = group.userId!!,
                    medicines = medicines!!,
                    customNameList = group.customNameList ?: listOf(),
                    imageUrlList = group.imageUrlList ?: listOf(),
                    startedAt = LocalDate.parse(group.startedAt),
                    finishedAt = LocalDate.parse(group.finishedAt),
                    daysOfWeeks = daysWeek ?: listOf(),
                    alarms = group.alarms ?: listOf(),
                    hasTaken = group.hasTaken ?: listOf()

                )
            }
        }.onFailure {
            Log.w(TAG, "getMyGroups failed) msg: ${it.message}")
        }.getOrDefault(listOf())
    }

    override suspend fun saveNewGroup(medicineGroup: MedicineGroup) {
        runCatching {
            medicineGroupDataSource.addSingleGroup(medicineGroup)
        }.onFailure {
            Log.w(TAG, "saveNewGroup failed) msg: ${it.message}")
        }
    }

    override suspend fun removeGroup(medicineGroup: MedicineGroup) {
        runCatching {
            medicineGroupDataSource.removeGroup(medicineGroup)
        }.onFailure {
            Log.w(TAG, "removeGroup failed) msg: ${it.message}")
        }
    }

    override suspend fun notifyTaken(medicineGroup: MedicineGroup, takenTime: String): MedicineGroup {
        return kotlin.runCatching {
            val format = "${LocalDate.now()} $takenTime"
            val takenGroup = medicineGroup.copy(
                hasTaken = medicineGroup.hasTaken.toMutableList().apply { add(format) }
            )
            medicineGroupDataSource.updateGroup(takenGroup)

            takenGroup
        }.onFailure {
            Log.w(TAG, "notifyTaken failed) msg: ${it.message}")
        }.getOrDefault(medicineGroup)
    }
}