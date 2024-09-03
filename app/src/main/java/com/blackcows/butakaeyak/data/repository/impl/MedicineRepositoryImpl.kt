package com.blackcows.butakaeyak.data.repository.impl

import com.blackcows.butakaeyak.data.models.Drug
import com.blackcows.butakaeyak.data.models.Pill
import com.blackcows.butakaeyak.data.repository.MedicineRepository
import com.blackcows.butakaeyak.data.toMap
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import javax.inject.Inject

class MedicineRepositoryImpl @Inject constructor(
): MedicineRepository {
    private val db = Firebase.firestore
    override fun addDrug(drug: Drug, callback: (Boolean) -> Unit) {
        db.collection("drugs").document(drug.id!!)
            .set(drug.toMap())
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener{ callback(false) }
    }

    override fun searchDrugs(name: String, callback: (List<Drug>) -> Unit) {
        db.collection("drugs")
            .whereEqualTo("name", name)
            .get()
            .addOnSuccessListener { documents ->
                val list = documents.toObjects(Drug::class.java)
                callback(list)
            }.addOnFailureListener {
                callback(listOf())
            }
    }

    override fun addPill(pill: Pill, callback: (Boolean) -> Unit) {
        db.collection("pills").document(pill.id!!)
            .set(pill.toMap())
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener{ callback(false) }
    }

    override fun searchPills(name: String, callback: (List<Pill>) -> Unit) {
        db.collection("drugs")
            .whereEqualTo("name", name)
            .get()
            .addOnSuccessListener { documents ->
                val list = documents.toObjects(Pill::class.java)
                callback(list)
            }.addOnFailureListener {
                callback(listOf())
            }
    }

}