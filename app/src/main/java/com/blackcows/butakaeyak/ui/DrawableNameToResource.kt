package com.blackcows.butakaeyak.ui

import com.blackcows.butakaeyak.R

enum class DrawableNameToResource(val drawableName: String, val resourceId: Int) {
    MedicineType1("medicine_type_1" , R.drawable.medicine_type_1),
    MedicineType2("medicine_type_2" , R.drawable.medicine_type_2),
    MedicineType3("medicine_type_3" , R.drawable.medicine_type_3),
    MedicineType4("medicine_type_4" , R.drawable.medicine_type_4),
    MedicineType5("medicine_type_5" , R.drawable.medicine_type_5),
    MedicineType6("medicine_type_6" , R.drawable.medicine_type_6),
    MedicineType7("medicine_type_7" , R.drawable.medicine_type_7),
    MedicineType8("medicine_type_8" , R.drawable.medicine_type_8),
    MedicineType9("medicine_type_9" , R.drawable.medicine_type_9),
    MedicineType10("medicine_type_10" , R.drawable.medicine_type_10),
    MedicineType11("medicine_type_11" , R.drawable.medicine_type_11),
    MedicineType12("medicine_type_12" , R.drawable.medicine_type_12),
    MedicineType13("medicine_type_13" , R.drawable.medicine_type_13),
    MedicineType14("medicine_type_14" , R.drawable.medicine_type_14)
}

fun getDrawableNames (name: String): Int {
    DrawableNameToResource.entries.forEach {it ->
        if (it.drawableName == name) {
            return it.resourceId
        }
    }
    throw Exception("There is no drawable resource.")
}