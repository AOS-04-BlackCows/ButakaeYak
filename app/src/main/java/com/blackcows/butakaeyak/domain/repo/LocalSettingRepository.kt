package com.blackcows.butakaeyak.domain.repo

interface LocalSettingRepository {
    fun saveDefaultAlarms(alarms: List<String>)
    fun getDefaultAlarms(): List<String>
}