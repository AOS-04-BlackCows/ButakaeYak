package com.blackcows.butakaeyak.domain.repo

import com.blackcows.butakaeyak.data.models.MedicineGroup

interface MedicineGroupRepository {
    suspend fun getMyGroups(userId: String): List<MedicineGroup>
    suspend fun saveNewGroup(medicineGroup: MedicineGroup)
    suspend fun removeGroup(medicineGroup: MedicineGroup)
    suspend fun notifyTaken(medicineGroup: MedicineGroup, takenTime: String): MedicineGroup
}