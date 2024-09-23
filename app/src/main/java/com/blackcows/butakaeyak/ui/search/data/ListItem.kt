package com.blackcows.butakaeyak.ui.search.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class ListItem : Parcelable {
    @Parcelize
    data class PillResultItem(
        val entpName: String?,
        val itemName: String?,
        val itemSeq: String?,        // id
        val efcyQesitm: String?,     //효능
        val itemImage: String?
    ) : ListItem()
    @Parcelize
    data class FeedItem(val name : String) : ListItem()
}