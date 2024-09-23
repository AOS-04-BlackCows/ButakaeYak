package com.blackcows.butakaeyak.data.source.firebase

import com.blackcows.butakaeyak.data.models.MyPharmacy
import com.blackcows.butakaeyak.data.source.link.MyPharmacyDataSource
import com.blackcows.butakaeyak.data.toMap
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RemoteMyPharmacyDataSource @Inject constructor(

): MyPharmacyDataSource {

    companion object {
        private const val TAG = "RemoteMyPharmacyDataSource"
        private const val MY_PHARMACY_COLLECTION = "myPharmacies"

        private const val PHARMACY_ID = "pharmacyId"
        private const val USER_ID = "userId"

        val NOT_REGISTERED_MY_PHARMACY = object : Exception() {
            override val message: String
                get() = "등록되지 않은 MyPharmacy에 접근 시도함"
        }
    }
    private val db = Firebase.firestore


    override suspend fun getMyPharmacies(userId: String): List<MyPharmacy> {
        return db.collection(MY_PHARMACY_COLLECTION)
            .whereEqualTo(USER_ID, userId)
            .get().await().toObjects(MyPharmacy::class.java)
    }

    override suspend fun saveMyPharmacies(pharmacies: List<MyPharmacy>) {
        coroutineScope {
            val tasks = pharmacies.map {
                async {
                    db.collection(MY_PHARMACY_COLLECTION)
                        .add(it.toMap())
                        .await()
                }
            }
            tasks.awaitAll()
        }
    }

    override suspend fun addSinglePharmacy(pharmacy: MyPharmacy) {
        db.collection(MY_PHARMACY_COLLECTION)
            .add(pharmacy.toMap())
            .await()
    }

    override suspend fun removePharmacy(pharmacy: MyPharmacy) {
        val obj = db.collection(MY_PHARMACY_COLLECTION)
            .whereEqualTo(USER_ID, pharmacy.userId)
            .whereEqualTo(PHARMACY_ID, pharmacy.pharmacyId)
            .get().await()

        if(obj.documents.isNotEmpty()) {
            obj.documents.forEach {
                println(it.id)
            }
            val id = obj.documents[0].id

            db.collection(MY_PHARMACY_COLLECTION)
                .document(id)
                .delete().await()
        } else {
            throw NOT_REGISTERED_MY_PHARMACY
        }
    }
}