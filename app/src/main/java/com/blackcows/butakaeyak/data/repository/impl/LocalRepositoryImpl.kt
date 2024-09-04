package com.blackcows.butakaeyak.data.repository.impl

import com.blackcows.butakaeyak.data.repository.LocalRepository
import com.blackcows.butakaeyak.data.source.LocalDataSource
import com.blackcows.butakaeyak.ui.take.data.MyMedicine
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource
): LocalRepository {
    override fun getMyMedicines(): List<MyMedicine>
        = localDataSource.getMyMedicines()


    override fun saveMyMedicines(list: List<MyMedicine>) {
        localDataSource.saveMyMedicines(list)
    }

    override fun addToMyMedicine(myMedicine: MyMedicine) {
        localDataSource.addMyMedicines(myMedicine)
    }
}