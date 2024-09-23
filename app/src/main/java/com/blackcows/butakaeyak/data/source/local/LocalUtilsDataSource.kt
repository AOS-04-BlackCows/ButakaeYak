package com.blackcows.butakaeyak.data.source.local

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.blackcows.butakaeyak.data.models.AutoLoginData
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LocalUtilsDataSource @Inject constructor(
    @ApplicationContext context: Context
) {
    companion object {
        private const val TAG = "LocalUtilsDataSource"

        private const val APP_SHARED_PREFS = "BUAKAEYAK"
        private const val LOGIN_DATA = "LOGIN_DATA"
        private const val IS_LOGIN = "IS_LOGIN"
    }

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    fun isSignIn(): Boolean {
        return sharedPreferences.getBoolean(IS_LOGIN, false)
    }
    fun setSignIn(isSignIn: Boolean) {
        editor.putBoolean(IS_LOGIN, isSignIn)
    }

    fun saveAutoLoginData(loginData: AutoLoginData) {
        val gson = Gson()
        val json = gson.toJson(loginData)
        editor.putString(LOGIN_DATA, json).apply()
    }

    fun getAutoLoginData(): AutoLoginData? {
        return sharedPreferences.getString(LOGIN_DATA, null)?.let {
            val gson = Gson()
            gson.fromJson(it, AutoLoginData::class.java)
        }
    }

    fun deleteAutoLoginData() {
        editor.remove(LOGIN_DATA).apply()
    }


}