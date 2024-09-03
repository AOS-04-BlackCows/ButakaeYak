package com.blackcows.butakaeyak.data.dto

import android.os.Parcelable
import com.blackcows.butakaeyak.data.models.Drug
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Parcelize
data class DrugInfoDto(
    val header: DrugHeader,
    val body: DrugBody
): Parcelable

@Parcelize
data class DrugHeader(
    val resultCode: String,
    val resultMsg: String,
): Parcelable

@Parcelize
data class DrugBody(
    val pageNo: Int,
    val totalCount: Int,
    val numOfRows: Int,
    val items: List<DrugItem>
): Parcelable

@Parcelize
data class DrugItem(
    val entpName: String?,
    val itemName: String?,
    val itemSeq: String?,        // id
    val efcyQesitm: String?,     //효능
    val useMethodQesitm: String?,    //사용법
    val atpnWarnQesitm: String?,     //경고사항
    val atpnQesitm: String?,         //주의사항
    val intrcQesitm: String?,        //상호작용
    val seQesitm: String?,           //부작용
    val depositMethodQesitm: String?,    //보관법
    val openDe: String?,
    val updateDe: String?,
    val itemImage: String?
): Parcelable {
    fun toDrug() = Drug(
        id = itemSeq,
        name = itemName,
        enterprise = entpName,
        effect = efcyQesitm,
        instructions = useMethodQesitm,
        warning = atpnWarnQesitm,
        caution = atpnQesitm,
        interaction = intrcQesitm,
        sideEffect = seQesitm,
        storingMethod = depositMethodQesitm,
        openDate = LocalDate.parse(openDe!!.split(" ")[0], DateTimeFormatter.ofPattern("yyyy-MM-dd")) ,
        updateDate = LocalDate.parse(updateDe!!.split(" ")[0], DateTimeFormatter.ofPattern("yyyy-MM-dd")),
        imageUrl = itemImage
    )
}
