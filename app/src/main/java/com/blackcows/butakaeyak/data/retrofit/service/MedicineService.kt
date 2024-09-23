package com.blackcows.butakaeyak.data.retrofit.service

import com.blackcows.butakaeyak.BuildConfig
import com.blackcows.butakaeyak.data.dto.DrugInfoDto
import retrofit2.http.GET
import retrofit2.http.Query

interface MedicineService {
    @GET("DrbEasyDrugInfoService/getDrbEasyDrugList")
    suspend fun getDrugInfo(
        @Query("itemName") name: String,
        @Query("pageNo") page: Int = 1,
        @Query("numOfRows") rows: Int = 3,
        @Query("type") type: String = "json",
        @Query("serviceKey") key: String = BuildConfig.DRUG_INFO_KEY,
    ): DrugInfoDto
}