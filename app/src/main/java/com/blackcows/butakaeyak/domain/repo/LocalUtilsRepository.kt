package com.blackcows.butakaeyak.domain.repo

import com.blackcows.butakaeyak.data.models.AutoLoginData

interface LocalUtilsRepository {
    fun hasLogin(): Boolean
    fun saveAutoLoginData(data: AutoLoginData)
    fun getAutoLoginData(): AutoLoginData?
    fun removeAutoLoginData()
    //fun saveAlarms()
}