package com.blackcows.butakaeyak.data.repository.impl

import android.util.Log
import com.blackcows.butakaeyak.data.WeekDayUtils
import com.blackcows.butakaeyak.data.models.MedicineGroup
import com.blackcows.butakaeyak.data.models.Memo
import com.blackcows.butakaeyak.data.source.api.MedicineInfoDataSource
import com.blackcows.butakaeyak.data.source.firebase.MedicineDataSource
import com.blackcows.butakaeyak.data.source.firebase.MemoDataSource
import com.blackcows.butakaeyak.data.source.firebase.RemoteMedicineGroupDataSource
import com.blackcows.butakaeyak.data.source.link.MedicineGroupDataSource
import com.blackcows.butakaeyak.data.source.local.LocalMedicineGroupDataSource
import com.blackcows.butakaeyak.data.source.local.LocalUtilsDataSource
import com.blackcows.butakaeyak.domain.repo.MemoRepository
import java.time.LocalDate
import javax.inject.Inject

class MemoRepositoryImpl @Inject constructor(
    private val memoDataSource: MemoDataSource,
    private val localMedicineGroupDataSource: LocalMedicineGroupDataSource,
    private val remoteMedicineGroupDataSource: RemoteMedicineGroupDataSource,
    private val medicineInfoDataSource: MedicineInfoDataSource,
    private val localUtilsDataSource: LocalUtilsDataSource
): MemoRepository {

    companion object {
        private const val TAG = "MemoRepositoryImpl"
    }

    private val medicineGroupDataSource
    = if(localUtilsDataSource.isSignIn()) remoteMedicineGroupDataSource
    else localMedicineGroupDataSource

    override suspend fun getMemosByUserId(userId: String): List<Memo> {
        return kotlin.runCatching {
            memoDataSource.getMemosFromWhen(userId, LocalDate.parse("1999-12-12")).map { response ->
                val group = medicineGroupDataSource.getMedicineGroupById(response.groupId)!!
                val medicines = group.medicineIdList?.map { id ->
                    medicineInfoDataSource.searchMedicinesWithId(id)[0]
                }
                val daysWeek = group.daysOfWeeks?.map { week ->
                    WeekDayUtils.fromKorean(week)
                }

                response.toMemo(MedicineGroup(
                    id = group.id!!,
                    name = group.name!!,
                    userId = group.userId!!,
                    medicines = medicines!!,
                    customNameList = group.customNameList!!,
                    imageUrlList = group.imageUrlList ?: listOf(),
                    startedAt = LocalDate.parse(group.startedAt),
                    finishedAt = LocalDate.parse(group.finishedAt),
                    daysOfWeeks = daysWeek ?: listOf(),
                    alarms = group.alarms ?: listOf(),
                    hasTaken = group.hasTaken ?: listOf()
                ))
            }
        }.onFailure {
                Log.w(TAG, "getMemosByUserId Failed) msg: ${it.message}")
        }.getOrDefault(listOf())
    }

    override suspend fun getMemoByMedicineGroupId(groupId: String): List<Memo> {
        return kotlin.runCatching {
            memoDataSource.getMemoByGroupId(groupId).map {
                val group = medicineGroupDataSource.getMedicineGroupById(groupId)!!

                val medicines = group.medicineIdList?.map { id ->
                    medicineInfoDataSource.searchMedicinesWithId(id)[0]
                }
                val daysWeek = group.daysOfWeeks?.map { week ->
                    WeekDayUtils.fromKorean(week)
                }

                it.toMemo(MedicineGroup(
                    id = group.id!!,
                    name = group.name!!,
                    userId = group.userId!!,
                    medicines = medicines!!,
                    customNameList = group.customNameList!!,
                    imageUrlList = group.imageUrlList ?: listOf(),
                    startedAt = LocalDate.parse(group.startedAt),
                    finishedAt = LocalDate.parse(group.finishedAt),
                    daysOfWeeks = daysWeek ?: listOf(),
                    alarms = group.alarms ?: listOf(),
                    hasTaken = group.hasTaken ?: listOf()
                ))
            }
        }.onFailure {
            Log.w(TAG, "getMemoByMedicineGroupId Failed) msg: ${it.message}")
        }.getOrDefault(listOf())
    }

    override suspend fun getMemosFromWhen(userId: String, createdAt: LocalDate): List<Memo> {
        return runCatching {
            memoDataSource.getMemosFromWhen(userId, createdAt).map {
                val group = medicineGroupDataSource.getMedicineGroups(userId).firstOrNull { g ->
                    g.id == it.groupId
                }!!

                val medicines = group.medicineIdList?.map { id ->
                    medicineInfoDataSource.searchMedicinesWithId(id)[0]
                }
                val daysWeek = group.daysOfWeeks?.map { week ->
                    WeekDayUtils.fromKorean(week)
                }

                it.toMemo(MedicineGroup(
                    id = group.id!!,
                    name = group.name!!,
                    userId = group.userId!!,
                    medicines = medicines!!,
                    customNameList = group.customNameList!!,
                    imageUrlList = group.imageUrlList ?: listOf(),
                    startedAt = LocalDate.parse(group.startedAt),
                    finishedAt = LocalDate.parse(group.finishedAt),
                    daysOfWeeks = daysWeek ?: listOf(),
                    alarms = group.alarms ?: listOf(),
                    hasTaken = group.hasTaken ?: listOf()
                ))
            }
        }.onFailure {
            Log.w(TAG, "getMemosFromWhen Failed) msg: ${it.message}")
        }.getOrDefault(listOf())
    }

    override suspend fun createMemo(memo: Memo) {
        runCatching {
            memoDataSource.saveMemo(memo)
        }.onFailure {
            Log.w(TAG, "createMemo Failed) msg: ${it.message}")
        }
    }

    override suspend fun editMemo(memo: Memo, content: String) {
        runCatching {
            val edited = memo.copy(
                content = content,
                updatedAt = LocalDate.now()
            )
            memoDataSource.editMemo(edited)
        }.onFailure {
            Log.w(TAG, "editMemo Failed) msg: ${it.message}")
        }
    }

    override suspend fun deleteMemo(memo: Memo) {
        runCatching {
            memoDataSource.deleteMemo(memo)
        }.onFailure {
            Log.w(TAG, "deleteMemo Failed) msg: ${it.message}")
        }
    }

}