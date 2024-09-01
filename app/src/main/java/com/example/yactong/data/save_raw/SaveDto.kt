package com.example.yactong.data.save_raw

import android.content.SharedPreferences
import android.os.Parcelable
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize


@Parcelize
data class SaveDto(
    val numPerPage: Int,
    val notYetSaved: List<Int>
): Parcelable {
    companion object {
        private const val TAG = "SaveDto"

        fun get(preferences: SharedPreferences): SaveDto? {
            val gson = Gson()
            val json = preferences.getString(TAG, "")

            return if(json?.isNotEmpty() == true)  gson.fromJson(json, SaveDto::class.java)
                    else null
        }

        fun save(preferences: SharedPreferences, saveDto: SaveDto) {
            val editor = preferences.edit()
            val gson = Gson()
            val json = gson.toJson(saveDto)
            editor.putString(TAG, json)
            editor.apply()
        }
    }
}