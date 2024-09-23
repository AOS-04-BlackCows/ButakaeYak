package com.blackcows.butakaeyak.data.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MedicineDto(
    @SerializedName("header")
    val header: MedicineDtoHeader,
    @SerializedName("body")
    val body: MedicineDtoBody
): Parcelable


@Parcelize
data class MedicineDtoHeader(
    @SerializedName("resultCode")
    val code: String,
    @SerializedName("resultMsg")
    val msg: String
): Parcelable

@Parcelize
data class MedicineDtoBody(
    @SerializedName("numOfRows")
    val rows: String,

    @SerializedName("pageNo")
    val page: String,
    @SerializedName("totalCount")
    val totalCount: String,

    @SerializedName("items")
    val items: List<MedicineDtoItem>
): Parcelable


@Parcelize
data class MedicineDtoItem(
    @SerializedName("ITEM_SEQ")
    val itemSeq: String? = null,

    @SerializedName( "ITEM_NAME")
    val itemName: String? = null,

    @SerializedName("ENTP_NAME")
    val entpName: String? = null,

    @SerializedName("CHART")
    val chart: String? = null,

    @SerializedName("STORAGE_METHOD")
    val storageMethod: String? = null,

    @SerializedName("EE_DOC_DATA")
    val eeDocData: String,

    @SerializedName("UD_DOC_DATA")
    val udDocData: String,

    @SerializedName("NB_DOC_DATA")
    val nbDocData: String,
): Parcelable
