package com.blackcows.butakaeyak.data.source.firebase

import android.util.Log
import com.blackcows.butakaeyak.data.models.MedicineGroup
import com.blackcows.butakaeyak.data.models.MedicineGroupResponse
import com.blackcows.butakaeyak.data.source.link.MedicineGroupDataSource
import com.blackcows.butakaeyak.data.toMap
import com.blackcows.butakaeyak.data.toObjectWithId
import com.blackcows.butakaeyak.data.toObjectsWithId
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RemoteMedicineGroupDataSource @Inject constructor(

): MedicineGroupDataSource {

    companion object {
        private const val TAG = "RemoteMedicineGroupDataSource"

        private const val MEDICINE_GROUP_COLLECTION = "medicineGroups"

        private const val USER_ID = "userId"
        private const val STARTED_AT = "startedAt"
        private const val FINISHED_AT = "finishedAt"

        val NOT_REGISTERED_MEDICINE_GROUP = object : Exception() {
            override val message: String
                get() = "등록되지 않은 Medicine Group에 접근 시도함"
        }
    }
    private val db = Firebase.firestore
    override suspend fun getMedicineGroupById(groupId: String): MedicineGroupResponse? {
        return db.collection(MEDICINE_GROUP_COLLECTION)
            .document(groupId)
            .get().await().toObjectWithId<MedicineGroupResponse>()
    }

    override suspend fun getMedicineGroups(userId: String): List<MedicineGroupResponse> {
        return db.collection(MEDICINE_GROUP_COLLECTION)
            .whereEqualTo(USER_ID, userId)
            .get().await().toObjectsWithId<MedicineGroupResponse>()
    }

    override suspend fun addSingleGroup(group: MedicineGroup) {
        db.collection(MEDICINE_GROUP_COLLECTION)
            .add(group.toRequest().toMap()).await()
    }

    override suspend fun removeGroup(group: MedicineGroup) {
        val hasIt = db.collection(MEDICINE_GROUP_COLLECTION)
            .document(group.id)
            .get().await()

        if(hasIt != null) {
            db.collection(MEDICINE_GROUP_COLLECTION)
                .document(group.id)
                .delete()
                .await()
        } else {
            throw NOT_REGISTERED_MEDICINE_GROUP
        }
    }

    override suspend fun updateGroup(takenGroup: MedicineGroup) {
        db.collection(MEDICINE_GROUP_COLLECTION)
            .document(takenGroup.id)
            .set(takenGroup.toRequest())
            .await()
    }

}