package com.blackcows.butakaeyak.ui.user

import androidx.lifecycle.ViewModel
import com.blackcows.butakaeyak.domain.repo.LocalSettingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// 알람 설정 -> 로컬 저장
@HiltViewModel
class MypageViewModel @Inject constructor(
    private val localSettingRepository: LocalSettingRepository
): ViewModel() {

    // 알림 저장하는 메서드
    fun saveDefaultAlarms(alarms: List<String>) {
        localSettingRepository.saveDefaultAlarms(alarms)
    }
    fun getDefaultAlarms() : List<String> {
        return localSettingRepository.getDefaultAlarms()
    }
}