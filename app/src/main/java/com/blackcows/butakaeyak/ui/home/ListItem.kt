package com.blackcows.butakaeyak.ui.home

sealed class ListItem {
    data class PillResultItem(val name : String) : ListItem()
    data class FeedItem(val name : String) : ListItem()
}