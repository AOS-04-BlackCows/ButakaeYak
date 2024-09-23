package com.blackcows.butakaeyak.data.repository.impl

import com.blackcows.butakaeyak.domain.repo.LocalRepository
import com.blackcows.butakaeyak.data.source.LocalDataSource
import com.blackcows.butakaeyak.firebase.firebase_store.models.UserData
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

    override fun isMyMedicine(id: String): Boolean {
        return localDataSource.isItemChecked(id)
    }

    @Deprecated("")
    override fun cancelMyMedicine(id: String) {
        localDataSource.removeMyMedicine(id)
    }

    override fun saveUserData(userData: UserData) {
        //localDataSource.saveAutoLoginData(userData)
    }

    override fun hasSavedUserData(): UserData? {
        return null
    }

    override fun deleteUserData() {
        //localDataSource.deleteAutoLoginData()
    }
}