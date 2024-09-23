package com.blackcows.butakaeyak.data.source

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.blackcows.butakaeyak.data.models.KakaoPlacePharmacy
import com.blackcows.butakaeyak.firebase.firebase_store.models.UserData
import com.blackcows.butakaeyak.ui.take.data.MyMedicine
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    @ApplicationContext context: Context
) {
    companion object {
        private const val TAG = "LocalDataSource"
        private const val TAG_PHARMACY = "k3f_MapFragment_LocalDataSource"

        private const val APP_SHARED_PREFS = "BUAKAEYAK"
        private const val PHARMACY_SHARED_PREFS = "PHARMACYS"

        const val MY_MEDICINES = "MEDICINES_IN_CONSUMING"
        const val FAVORITE_MEDICINES = "MEDICINES_IN_INTEREST"
        const val FAVORITE_PHARMACY = "PHARMACY_IN_INTEREST"

        const val USER_DATA = "USER_DATA"
    }

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE)
    private val sharedPreferencesPharmacy: SharedPreferences = context.getSharedPreferences(
        PHARMACY_SHARED_PREFS, Activity.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()
    private val editorPharmacy = sharedPreferencesPharmacy.edit()

    fun getMyMedicines(): List<MyMedicine> {
        val list = sharedPreferences.getString(MY_MEDICINES, null)?.let {
            val gson = Gson()
            val type = object : TypeToken<List<MyMedicine>>() {}.type
            gson.fromJson(it, type)
        } ?: listOf<MyMedicine>()

        Log.d(TAG, "list Size: ${list.size}")

        return list
    }
    fun saveMyMedicines(myMedicines: List<MyMedicine>) {
        val gson = Gson()
        val json = gson.toJson(myMedicines)
        editor.putString(MY_MEDICINES, json).apply()
    }

    fun addMyMedicines(myMedicine: MyMedicine) {
        saveMyMedicines(
            listOf(myMedicine) + getMyMedicines()
        )
    }

    fun isItemChecked(id: String) : Boolean {
        return getMyMedicines().any {
            it.medicine.id == id
        }
    }

    fun removeMyMedicine(id: String) {
        val lists = getMyMedicines().toMutableList().filterNot {
            it.medicine.id == id
        }
        saveMyMedicines(lists)
    }



    //TODO: ????
//    fun loadAllData(context: android.content.Context, fileName: String) : Medicine {
//        val pref = context.getSharedPreferences(fileName, MODE_PRIVATE).all.toString()
//        Log.d(TAG,context.getSharedPreferences(fileName, MODE_PRIVATE).all.toString())
//        return parseJson(pref)
//    }
//    fun parseJson(jsonString: String): Medicine {
//        val gson = Gson()
//        return gson.fromJson(jsonString, Medicine::class.java)
//    }

    // Pharmacy
    fun getMyPharmacy(): List<KakaoPlacePharmacy> {
        val list = sharedPreferencesPharmacy.getString(PHARMACY_SHARED_PREFS, null)?.let {
            val gson = Gson()
            val type = object : TypeToken<List<KakaoPlacePharmacy>>() {}.type
            gson.fromJson(it, type)
        } ?: listOf<KakaoPlacePharmacy>()
        Log.d(TAG_PHARMACY, "list Size: ${list.size}")

        return list
    }
    fun saveMyPharmacy(myPharmacy: List<KakaoPlacePharmacy>) {
        val gson = Gson()
        val json = gson.toJson(myPharmacy)
        editorPharmacy.putString(PHARMACY_SHARED_PREFS, json).apply()
        Log.d(TAG_PHARMACY, "saveMyPharmacy() Run. myPharmacy.size: ${myPharmacy.size}")
    }

    fun addMyPharmacy(pharmacy: KakaoPlacePharmacy) {
        saveMyPharmacy(
            listOf(pharmacy) + getMyPharmacy()
        )
        Log.d(TAG_PHARMACY, "addMyPharmacy() Run. id: ${pharmacy.id}")
    }

    fun isPharmacyChecked(id: String) : Boolean {
        Log.d(TAG_PHARMACY, "isPharmacyChecked() Run. id: ${id}")
        return getMyPharmacy().any {
            it.id == id
        }
    }

    fun removeMyPharmacy(id: String) {
        val lists = getMyPharmacy().toMutableList().filterNot {
            it.id == id
        }
        saveMyPharmacy(lists)
        Log.d(TAG_PHARMACY, "removeMyPharmacy() Run. id: ${id}")
    }
}