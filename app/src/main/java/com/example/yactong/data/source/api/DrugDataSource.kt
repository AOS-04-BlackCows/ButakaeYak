package com.example.yactong.data.source.api

import android.util.Log
import com.example.yactong.data.models.Drug
import com.example.yactong.data.models.Pill
import com.example.yactong.data.retrofit.DrugApiService
import retrofit2.Retrofit
import javax.inject.Inject

class DrugDataSource @Inject constructor(
    private val retrofit: DrugApiService
) {
    private val tag = "DrugDataSource"

    suspend fun searchDrugs(name: String): List<Drug> {
        val list = mutableListOf<Drug>()
        val result = retrofit.getDrugInfo(name)
        if(result.header.resultCode == "00") {
            result.body.items.forEach {
                list.add(it.toDrug())
            }
        } else {
            Log.d(tag, "SearchDrugs(code: ${result.header.resultCode}): ${result.header.resultMsg}")
        }

        return list
    }

    suspend fun searchPills(name: String): List<Pill> {
        val list = mutableListOf<Pill>()
        val result = retrofit.getPillInfo(name)
        if(result.header.resultCode == "00") {
            result.body.items.forEach {
                list.add(it.toPill())
            }
        } else {
            Log.d(tag, "searchPills(code: ${result.header.resultCode}): ${result.header.resultMsg}")
        }

        return list
    }
}