package com.blackcows.butakaeyak.data.source.api

import com.blackcows.butakaeyak.data.models.KakaoPlacePharmacy
import com.blackcows.butakaeyak.data.models.MedicineDetail
import com.blackcows.butakaeyak.data.retrofit.service.MedicineInfoService
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import javax.inject.Inject

class MedicineInfoDataSource @Inject constructor(
    private val retrofit: MedicineInfoService
) {
    suspend fun searchMedicines(name: String): List<MedicineDetail>
        = retrofit.getMedicineInfo(name).body.items.map {

            MedicineDetail(
                name = it.itemName ?: "없음",
                id = it.itemSeq ?: "없음",
                entpName = it.entpName ?: "없음",
                shape = it.chart,
                effect = parseXml(it.eeDocData),
                instruction = parseXml(it.udDocData),
                caution = parseXml(it.nbDocData),
                storing = it.storageMethod
            )
        }?: listOf()

    suspend fun searchMedicinesWithId(id: String): List<MedicineDetail>
     = retrofit.getMedicineInfoWithId(id).body.items.map {
        MedicineDetail(
            name = it.itemName ?: "없음",
            id = it.itemSeq ?: "없음",
            entpName = it.entpName ?: "없음",
            shape = it.chart,
            effect = parseXml(it.eeDocData),
            instruction = parseXml(it.udDocData),
            caution = parseXml(it.nbDocData),
            storing = it.storageMethod
        )
    }


    private fun parseXml(xml: String): String {
        if(xml.isBlank()) return "없음"

        val factory = XmlPullParserFactory.newInstance()
        factory.isNamespaceAware = true
        val parser = factory.newPullParser()
        parser.setInput(xml.reader())

        val stringBuilder = StringBuilder()
        var eventType = parser.eventType

        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.TEXT -> {
                    val text = parser.text.trim()
                    // 공백이 아닌 텍스트만 추가
                    if (text.isNotEmpty()) {
                        stringBuilder.append(text).append("\n")
                    }
                }
                // START_TAG와 END_TAG는 무시
                XmlPullParser.START_TAG, XmlPullParser.END_TAG -> {
                    // 아무 작업도 하지 않음
                }
            }
            eventType = parser.next()
        }

        return stringBuilder.toString().replace(Regex("<[^>]+>"), "").trim() // 최종적으로 앞뒤 공백 제거
    }

}