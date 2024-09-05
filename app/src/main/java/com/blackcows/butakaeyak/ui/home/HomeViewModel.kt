package com.blackcows.butakaeyak.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.domain.GetMedicinesNameUseCase
import com.blackcows.butakaeyak.domain.home.GetPillUseCase
import com.blackcows.butakaeyak.ui.home.data.DataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "SearchResult"
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPillUseCase: GetPillUseCase,
    private val getMedicinesNameUseCase: GetMedicinesNameUseCase
) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text          //LiveData는 뭐 때문에 넣은거야??

    private val _medicineResult = MutableLiveData(listOf<Medicine>())
    val medicineResult get() = _medicineResult

    fun searchMedicinesWithName(name: String) {
        viewModelScope.launch {

            _medicineResult.value = getMedicinesNameUseCase.invoke(name)
        }
    }
}