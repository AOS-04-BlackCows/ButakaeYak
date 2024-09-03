package com.blackcows.butakaeyak.data.save_raw

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.util.Log
import com.blackcows.butakaeyak.data.models.Drug
import com.blackcows.butakaeyak.data.repository.DrugRepository
import com.blackcows.butakaeyak.data.retrofit.AiResultDto
import com.blackcows.butakaeyak.data.retrofit.Medicine
import com.blackcows.butakaeyak.data.toMap
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
        val failedIndex = mutableListOf<Int>()
        val saveDto = SaveDto.get(preferences) ?:
                    SaveDto(numPerPage, (1..pages).toList())

        var index = 1
        while (index <= saveDto.notYetSaved.lastIndex) {
            var isSuccess = true
            val num = saveDto.notYetSaved[index]
            try {
                Log.d(TAG, "$index/${saveDto.notYetSaved.size})---------------------------------------------------------")
                val list = getDrugs("", num, numPerPage)

                if (checkIsSaved(list[0])) {
                    Log.d(TAG, "$index) already Saved...")
                    index++
                    continue
                }

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

            if(!isSuccess) failedIndex.add(num)
            index++
        }

        SaveDto.save(
            preferences,
            SaveDto(numPerPage, failedIndex)
        )

        Log.d("SaveMedicineModule_Result", "left:${failedIndex.size}: ${failedIndex.joinToString()}")
    }

    private suspend fun checkIsSaved(drug: Drug): Boolean {
        try {

            val isSaved = db.collection("medicines")
                .whereEqualTo("id", drug.id!!)
                .get()
                .await().toObjects(Medicine::class.java)

            Log.d(TAG, "has Medicine(id: ${drug.id}?) ${isSaved.isNotEmpty()}")

            return isSaved.isNotEmpty()
        } catch (e: Exception) {
            Log.d(TAG, "CheckIsSaved Failed: ${e.message}")
            e.printStackTrace()
            return false
        }
    }

    private suspend fun saveInFireStore(drug: Drug, medicine: Medicine) {
        val data = medicine.toMap()
        try {
            db.collection("medicines").document(medicine.id!!)
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