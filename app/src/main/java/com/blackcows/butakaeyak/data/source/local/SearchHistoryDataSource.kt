package com.blackcows.butakaeyak.data.source.local

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SearchHistoryDataSource @Inject constructor(
    @ApplicationContext context: Context
) {
    companion object {
        private const val TAG = "SearchHistoryDataSource"

        private const val HISTORY_PREF = "BUAKAEYAK_HISTORY"

        const val MY_MEDICINES = "MEDICINES_IN_CONSUMING"
        const val QUERY_HISTORY = "QUERY_HISTORY"
        const val SEARCH_HISTORY = "SEARCH_HISTORY"
    }

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(HISTORY_PREF, Activity.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    fun getQueryHistory(): List<String> {
        return sharedPreferences.getStringSet(QUERY_HISTORY, setOf())?.toList() ?: listOf()
    }
    fun saveQueryHistory(query: String) {
        val list = getQueryHistory().toMutableList()
        list.add(query)

        editor.putStringSet(QUERY_HISTORY, list.toSet())
    }
    fun removeQueryHistory() {
        editor.remove(QUERY_HISTORY)
    }

    fun getMedicineDetailHistory(): List<String> {
        return sharedPreferences.getStringSet(SEARCH_HISTORY, setOf())?.toList() ?: listOf()
    }
    fun saveMedicineDetailHistory(id: String) {
        val list = getMedicineDetailHistory().toMutableList()
        list.add(id)

        editor.putStringSet(SEARCH_HISTORY, list.toSet())
    }
    fun removeMedicineDetailHistory() {
        editor.remove(SEARCH_HISTORY)
    }
}