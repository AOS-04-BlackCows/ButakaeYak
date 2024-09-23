package com.blackcows.butakaeyak.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.domain.GetMedicinesNameUseCase
import com.blackcows.butakaeyak.domain.repo.LocalUtilsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "SearchResult"
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getMedicinesNameUseCase: GetMedicinesNameUseCase,
    private val localUtilsRepository: LocalUtilsRepository
) : ViewModel() {

    private val queryHistory = MutableLiveData<String>()

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text          //뷰페이저의 현재 아이템(화면)용

    private val _medicineResult = MutableLiveData(listOf<Medicine>())
    val medicineResult get() = _medicineResult

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Init)
    val uiState = _uiState.asStateFlow()

    fun searchMedicinesWithName(name: String) {
        viewModelScope.launch {
            _uiState.value = SearchUiState.Loading
            _medicineResult.value = getMedicinesNameUseCase.invoke(name)
            _uiState.value = SearchUiState.SearchMedicinesSuccess(_medicineResult.value!!)
        }
    }

    fun getQueryHistory(){
        //TODO 검색한 기록 저장 (반환타입 List<String>)
//        queryHistory.value = localUtilsRepository.getQueryHistory()
    }

    fun getViewedHistory(){
        //TODO 검색후 클릭한 기록 저장 (반환타입 List<Medicine>)
//        queryHistory.value = localUtilsRepository.getViewedViewedHistory()
    }
}