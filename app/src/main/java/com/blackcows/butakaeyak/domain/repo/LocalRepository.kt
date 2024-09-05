package com.blackcows.butakaeyak.domain.repo

import com.blackcows.butakaeyak.ui.take.data.MyMedicine

interface LocalRepository {
    fun getMyMedicines(): List<MyMedicine>
    fun saveMyMedicines(list: List<MyMedicine>)
    fun addToMyMedicine(myMedicine: MyMedicine)
}