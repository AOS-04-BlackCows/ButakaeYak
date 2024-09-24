package com.blackcows.butakaeyak.data.repository.impl

import com.blackcows.butakaeyak.data.source.local.LocalSettingDataSource
import com.blackcows.butakaeyak.domain.repo.LocalSettingRepository
import javax.inject.Inject

class LocalSettingRepositoryImpl @Inject constructor(
    private val localSettingDataSource: LocalSettingDataSource
): LocalSettingRepository {
    override fun saveDefaultAlarms(alarms: List<String>) {
        localSettingDataSource.saveAlarms(alarms)
    }

    override fun getDefaultAlarms(): List<String> {
        return localSettingDataSource.getAlarms()
    }

}