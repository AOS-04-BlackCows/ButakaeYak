package com.blackcows.butakaeyak.data.dto


import android.os.Parcelable
import com.blackcows.butakaeyak.data.models.KakaoPlacePharmacy
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class KakaoMapDTO(
    @SerializedName("documents")
    val documents: List<KakaoDocuments>,
    @SerializedName("meta")
    val meta: KakaoMeta?
) : Parcelable

@Parcelize
data class KakaoMeta(
    @SerializedName("is_end")
    val isEnd: Boolean?,
    @SerializedName("pageable_count")
    val pageableCount: Int?,
    @SerializedName("same_name")
    val sameName: KakaoSameName?,
    @SerializedName("total_count")
    val totalCount: Int?
) : Parcelable

@Parcelize
data class KakaoSameName(
    @SerializedName("keyword")
    val keyword: String?,
    @SerializedName("region")
    val region: List<String?>?,
    @SerializedName("selected_region")
    val selectedRegion: String?
) : Parcelable

@Parcelize
data class KakaoDocuments (
    @SerializedName("place_name")
    val placeName: String?,
    @SerializedName("distance")
    val distance: String?,
    @SerializedName("place_url")
    val placeUrl: String?,
    @SerializedName("category_name")
    val categoryName: String?,
    @SerializedName("address_name")
    val addressName: String?,
    @SerializedName("road_address_name")
    val roadAddressName: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("category_group_code")
    val categoryGroupCode: String?,
    @SerializedName("category_group_name")
    val categoryGroupName: String?,
    @SerializedName("x")
    val x: String?,
    @SerializedName("y")
    val y: String?
) : Parcelable {
    fun toKakaoPlace() = KakaoPlacePharmacy(
        placeName = placeName?: "",
        distance = distance?: "",
        placeUrl = placeUrl?: "",
        categoryName = categoryName?: "",
        addressName = addressName?: "",
        roadAddressName = roadAddressName?: "",
        id = id?: "",
        phone = phone?: "",
        categoryGroupCode = categoryGroupCode?: "",
        categoryGroupName = categoryGroupName?: "",
        x = x?: "",
        y = y?: ""
    )
}