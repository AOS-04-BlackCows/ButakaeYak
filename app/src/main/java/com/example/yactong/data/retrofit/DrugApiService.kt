package com.example.yactong.data.retrofit

import com.example.yactong.BuildConfig
import com.example.yactong.data.dto.DrugInfoDto
import com.example.yactong.data.dto.PillInfoDto
import retrofit2.http.GET
import retrofit2.http.Query

interface DrugApiService {
     //drug: DrbEasyDrugInfoService
    // pill: MdcinGrnIdntfcInfoService01

    @GET("DrbEasyDrugInfoService/getDrbEasyDrugList")
    suspend fun getDrugInfo(
        @Query("itemName") name: String,
        @Query("pageNo") page: Int = 1,
        @Query("numOfRows") rows: Int = 3,
        @Query("type") type: String = "json",
        @Query("serviceKey") key: String = BuildConfig.DRUG_INFO_KEY,
    ): DrugInfoDto

    @GET("MdcinGrnIdntfcInfoService01/getMdcinGrnIdntfcInfoList01")
    suspend fun getPillInfo(
        @Query("item_name") name: String,
        @Query("pageNo") page: Int = 1,
        @Query("numOfRows") rows: Int = 3,
        @Query("type") type: String = "json",
        @Query("serviceKey") key: String = BuildConfig.DRUG_INFO_KEY,
    ): PillInfoDto
}