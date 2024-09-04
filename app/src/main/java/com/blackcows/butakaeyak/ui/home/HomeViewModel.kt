package com.blackcows.butakaeyak.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blackcows.butakaeyak.data.models.Drug
import com.blackcows.butakaeyak.data.models.Pill
import com.blackcows.butakaeyak.data.repository.LocalRepository
import com.blackcows.butakaeyak.domain.GetMedicinesNameUseCase
import com.blackcows.butakaeyak.domain.home.GetPillUseCase
import com.blackcows.butakaeyak.ui.example.UserUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPillUseCase: GetPillUseCase,
    private val getMedicinesNameUseCase: GetMedicinesNameUseCase
) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text          //LiveData는 뭐 때문에 넣은거야??

    private val _pillResult = MutableLiveData(mutableListOf<Pill>())
    private val pillResult get() = _pillResult

    fun searchPills(name: String) {
        getPillUseCase.invoke(name) { list ->
            Log.d("HomeViewModel", "SearchPills: Find ${list.size}'s result.")
            if(list.isNotEmpty()) {
                // 검색 결과 있음.
                pillResult.value = list.toMutableList()
            } else {
                // 검색 결과 없음.
            }
        }
    }

    fun searchMedicinesWithName(name: String) {
        getMedicinesNameUseCase.invoke(name) {
            if(it.isNotEmpty()) {
                //결과가 있을때
            } else {
                //결과가 없을때
            }
        }
    }

    fun getUserName(id: String) {
//        getUserNameUseCase.invoke(id) { userName ->
//            if(userName.isNotEmpty()) {
//
//            } else {
//
//            }
//        }
    }
}