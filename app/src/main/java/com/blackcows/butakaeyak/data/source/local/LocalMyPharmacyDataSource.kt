package com.blackcows.butakaeyak.data.source.local

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.blackcows.butakaeyak.data.models.KakaoPlacePharmacy
import com.blackcows.butakaeyak.data.models.MyPharmacy
import com.blackcows.butakaeyak.data.source.link.MyPharmacyDataSource
import com.blackcows.butakaeyak.firebase.firebase_store.models.UserData
import com.blackcows.butakaeyak.ui.take.data.MyMedicine
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LocalMyPharmacyDataSource @Inject constructor(
    @ApplicationContext context: Context
): MyPharmacyDataSource {
    companion object {
        private const val TAG = "PHARMACY_DATASOURCE"
        private const val PHARMACY_SHARED_PREFS = "PHARMACYS"
        private const val FAVORITE_PHARMACY = "PHARMACY_IN_INTEREST"
    }

    private val sharedPreferencesPharmacy: SharedPreferences = context.getSharedPreferences(
        PHARMACY_SHARED_PREFS, Activity.MODE_PRIVATE)
    private val editorPharmacy = sharedPreferencesPharmacy.edit()

    override suspend fun getMyPharmacies(userId: String): List<MyPharmacy> {
        val list = sharedPreferencesPharmacy.getString(FAVORITE_PHARMACY, null)?.let {
            val gson = Gson()
            val type = object : TypeToken<List<MyPharmacy>>() {}.type
            gson.fromJson(it, type)
        } ?: listOf<MyPharmacy>()
        Log.d(TAG, "list Size: ${list.size}")

        return list
    }

    override suspend fun saveMyPharmacies(pharmacies: List<MyPharmacy>) {
        val gson = Gson()
        val json = gson.toJson(pharmacies)
        editorPharmacy.putString(FAVORITE_PHARMACY, json).apply()
        Log.d(TAG, "saveMyPharmacy() Run. myPharmacy.size: ${pharmacies.size}")
    }

    override suspend fun addSinglePharmacy(pharmacy: MyPharmacy) {
        saveMyPharmacies(
            listOf(pharmacy) + getMyPharmacies("")
        )
    }

    override suspend fun removePharmacy(pharmacy: MyPharmacy) {
        val lists = getMyPharmacies("").toMutableList().filterNot {
            it == pharmacy
        }
        saveMyPharmacies(lists)
    }
}