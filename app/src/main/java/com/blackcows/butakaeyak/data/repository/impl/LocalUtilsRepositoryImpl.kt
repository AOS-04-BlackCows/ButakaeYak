package com.blackcows.butakaeyak.data.repository.impl

import com.blackcows.butakaeyak.data.models.AutoLoginData
import com.blackcows.butakaeyak.data.source.local.LocalUtilsDataSource
import com.blackcows.butakaeyak.domain.repo.LocalUtilsRepository
import javax.inject.Inject

class LocalUtilsRepositoryImpl @Inject constructor(
    private val localUtilsDataSource: LocalUtilsDataSource
): LocalUtilsRepository {
    override fun hasLogin(): Boolean {
        return localUtilsDataSource.isSignIn()
    }

    override fun saveAutoLoginData(data: AutoLoginData) {
        localUtilsDataSource.saveAutoLoginData(data)
    }

    override fun getAutoLoginData(): AutoLoginData? {
        return localUtilsDataSource.getAutoLoginData()
    }

    override fun removeAutoLoginData() {
        localUtilsDataSource.deleteAutoLoginData()
    }
}