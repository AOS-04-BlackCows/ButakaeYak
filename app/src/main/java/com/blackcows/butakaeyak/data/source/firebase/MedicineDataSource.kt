package com.blackcows.butakaeyak.data.source.firebase

import android.util.Log
import com.algolia.search.saas.Client
import com.algolia.search.saas.Query
import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.data.retrofit.DrugApiService
import com.google.gson.Gson
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class MedicineDataSource @Inject constructor(
    private val retrofit: DrugApiService,
    private val algoliaClient: Client
) {
    companion object {
        private const val INDEX_NAME = "medicines"
        private const val TAG = "MedicineDataSource"
    }

    private val medicineIndex by lazy {
        algoliaClient.getIndex(INDEX_NAME)
    }

    suspend fun searchMedicinesByName(name: String): List<Medicine> {
        val result = mutableListOf<Medicine>()
        val facet = "name"

        val query = Query(name).apply {
            advancedSyntax = true
            setRestrictSearchableAttributes(facet)
        }

        val jsonArray = medicineIndex.searchSync(query)

        jsonArray?.getJSONArray("hits")?.let { medicines ->
            println("hits size: ${medicines.length()}")
            Log.d(TAG, "hits size: ${medicines.length()}")
            val gson = Gson()

            val seq = (0 until medicines.length()).asSequence().map { medicines.getJSONObject(it) }
            for(json in seq) {
                val data = gson.fromJson(json.toString(), Medicine::class.java)
                result.add(getImageUrl(data))
            }
        }

        return result
    }

    suspend fun searchMedicinesByNames(names: String): List<Medicine> {
        val result = mutableListOf<Medicine>()
        val facet = "name"

        val queryList = names.split(" ").map {
            Query(it).apply {
                advancedSyntax = true
                setRestrictSearchableAttributes(facet)
            }
        }

        println(queryList.joinToString())

        val jsonArray = suspendCoroutine { continuation ->
            medicineIndex.multipleQueriesAsync(
                queryList,
                Client.MultipleQueriesStrategy.NONE) { jsonObj, e ->
                continuation.resume(jsonObj)
            }
        }

        println(jsonArray.toString())

        jsonArray?.getJSONArray("hits")?.let { medicines ->
            println("hits size: ${medicines.length()}")
            Log.d(TAG, "hits size: ${medicines.length()}")
            val gson = Gson()

            val seq = (0 until medicines.length()).asSequence().map { medicines.getJSONObject(it) }
            for(json in seq) {
                val data = gson.fromJson(json.toString(), Medicine::class.java)
                result.add(getImageUrl(data))
            }
        }

        return result
    }

    private suspend fun getImageUrl(medicine: Medicine): Medicine {
        val item = retrofit.getDrugInfo("").body.items

        return if(item.isNotEmpty())  medicine.copy(
            imageUrl = item[0].itemImage
        ) else medicine
    }

}