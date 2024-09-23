package com.blackcows.butakaeyak.ui.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blackcows.butakaeyak.domain.repo.LocalUtilsRepository
import com.blackcows.butakaeyak.ui.take.data.AlarmItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// 알람 설정 -> 로컬 저장
@HiltViewModel
class MypageViewModel @Inject constructor(private val localUtilsRepository: LocalUtilsRepository) :
    ViewModel() {

    // 알림 저장하는 메서드
    fun saveDefultAlams(alams: List<String>) {
        //TODO : 알림 설정 해놓기
    }
}