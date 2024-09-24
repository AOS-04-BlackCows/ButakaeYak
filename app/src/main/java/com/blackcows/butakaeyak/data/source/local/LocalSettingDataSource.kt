package com.blackcows.butakaeyak.data.source.local

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.blackcows.butakaeyak.data.source.local.LocalUtilsDataSource.Companion
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LocalSettingDataSource @Inject constructor(
    @ApplicationContext context: Context
){
    companion object {
        private const val TAG = "LocalSettingDataSource"

        private const val SETTING_PREFS = "BUAKAEYAK_SETTING"

        private const val ALARMS = "ALARMS"
    }

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(SETTING_PREFS, Activity.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    fun saveAlarms(alarms: List<String>) {
        editor.putStringSet(ALARMS, alarms.toSet())
    }

    fun getAlarms(): List<String> {
        return sharedPreferences.getStringSet(ALARMS, setOf())?.toList() ?: listOf()
    }
}