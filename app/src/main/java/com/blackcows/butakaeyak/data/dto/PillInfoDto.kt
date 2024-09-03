package com.blackcows.butakaeyak.data.dto

import android.os.Parcelable
import com.blackcows.butakaeyak.data.models.Pill
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PillInfoDto(
    val header: PillHeader,
    val body: PillBody
): Parcelable


@Parcelize
data class PillHeader(
    val resultCode: String,
    val resultMsg: String,
): Parcelable

@Parcelize
data class PillBody(
    val pageNo: Int,
    val totalCount: Int,
    val numOfRows: Int,
    val items: List<PillItem>
): Parcelable

@Parcelize
data class PillItem(
    @SerializedName("ITEM_SEQ") val id: String,
    @SerializedName("ITEM_NAME") val name: String,
    @SerializedName("ENTP_SEQ") val entpId: String?,
    @SerializedName("ENTP_NAME") val entpName: String?,
    @SerializedName("CHART") val feature: String?,
    @SerializedName("ITEM_IMAGE") val imageUrl: String?,
    @SerializedName("PRINT_FRONT") val front: String?,      // 앞 프린팅
    @SerializedName("PRINT_BACK") val back: String,         // 뒤 프린팅
    @SerializedName("DRUG_SHAPE") val shape: String,
    @SerializedName("COLOR_CLASS1") val color1: String,
    @SerializedName("COLOR_CLASS2") val color2: String?,
    @SerializedName("LINE_FRONT") val lineFront: String?,   // 앞 분할선
    @SerializedName("LINE_BACK") val lineBack: String?,     // 뒤 분할선
    @SerializedName("LENG_LONG") val lengthLong: String?,
    @SerializedName("LENG_SHORT") val lengthShort: String?,
    @SerializedName("CLASS_NAME") val classification: String?,
    @SerializedName("ETC_OTC_NAME") val etcOrOtc: String?,  //일반의약품? 전문의약품?
): Parcelable {
    fun toPill() = Pill(
        id = id,
        name = name,
        enterprise = entpName,
        feature = feature,
        front = front,
        back = back,
        shape = shape,
        color1 = color1,
        color2 = color2,
        lineFront = lineFront,
        lineBack = lineBack,
        lengthLong = lengthLong,
        lengthShort = lengthShort,
        classification = classification,
        etcOrOtc = etcOrOtc,
        imageUrl = imageUrl
    )
}