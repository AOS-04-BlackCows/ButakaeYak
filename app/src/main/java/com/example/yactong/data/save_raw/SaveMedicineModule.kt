package com.example.yactong.data.save_raw

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.util.Log
import com.example.yactong.data.models.Drug
import com.example.yactong.data.repository.DrugRepository
import com.example.yactong.data.retrofit.AiResultDto
import com.example.yactong.data.retrofit.Medicine
import com.example.yactong.data.toMap
import com.google.firebase.Firebase
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import okhttp3.ResponseBody
import java.lang.reflect.Type
import java.util.prefs.Preferences
import javax.inject.Inject
import kotlin.concurrent.timer

class SaveMedicineModule @Inject constructor(
    private val drugRepository: DrugRepository,
    private val aiApiService: AiApiService
) {
    // total items count: 4777
    // max items per page: 100

    private val TAG = "SaveMedicineModule"
    private val db = Firebase.firestore

    private val numPerPage = 5
    private val pages = 4777/numPerPage + if(4777%numPerPage != 0) 1 else 0

    suspend fun doSave(preferences: SharedPreferences, editor: Editor) {
        var index = 1
        val failedIndex = mutableListOf<Int>()

        val saveDto = SaveDto.get(preferences) ?:
                    SaveDto(numPerPage, (1..pages).toList())

        while (index <= saveDto.notYetSaved.size) {
            var isSuccess = true
            try {
                Log.d(TAG, "$index/${saveDto.notYetSaved.size})---------------------------------------------------------")
                val list = getDrugs("", index, numPerPage)
                val medicineList = summarize(list)
                medicineList.forEachIndexed { i, it ->
                    saveInFireStore(list[i], it)
                }
                isSuccess = true
            } catch (e: Exception) {
                Log.e(TAG, "Failed at $index: ${e.message}")
                e.printStackTrace()
                isSuccess = false
            }

            delay(30000L)

            if(!isSuccess) failedIndex.add(index)
            index++
        }

        SaveDto.save(
            preferences,
            SaveDto(numPerPage, failedIndex)
        )

        Log.d("SaveMedicineModule_Result", failedIndex.joinToString())
    }

    private suspend fun saveInFireStore(drug: Drug, medicine: Medicine) {
        val data = medicine.toMap()
        try {
            db.collection("medicines").document(medicine.id)
                .set(data, SetOptions.merge())
                .await()

            Log.d(TAG, "Succeed Storing data: ${medicine.id}, ${medicine.name}")
        } catch (e: Exception) {
            Log.i(TAG, "Failed Storing data: ${medicine.id}, ${medicine.name}: ${e.message}")
        }
    }

    private suspend fun summarize(list: List<Drug>): List<Medicine> {
        val aiMsg = makeAiMessage(list)
        val medicineJson = aiApiService.getSummarize(AiRequestDto(listOf(aiMsg))).choice[0].msg.medicineJson.apply { replace("```json", "") }

        val gson = Gson()
        val type: Type = object : TypeToken<List<Medicine>>() {}.type
        val medicines: List<Medicine> = gson.fromJson(medicineJson, type)

        return medicines
    }

    private suspend fun getDrugs(name: String, page: Int, itemNum: Int): List<Drug> {
        return drugRepository.searchDrugs(name, page, itemNum)
    }
}