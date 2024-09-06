package com.blackcows.butakaeyak.ui.take

import androidx.lifecycle.ViewModel
import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.domain.take.GetMyMedicinesUseCase
import com.blackcows.butakaeyak.domain.take.GetTodayMedicineUseCase
import com.blackcows.butakaeyak.ui.example.UserUiState
import com.blackcows.butakaeyak.ui.take.data.MedicineAtTime
import com.blackcows.butakaeyak.ui.take.data.MyMedicine
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.util.date.WeekDay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MyTakeViewModel @Inject constructor(
    private val getTodayMedicineUseCase: GetTodayMedicineUseCase,
    private val getMyMedicinesUseCase: GetMyMedicinesUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow<TakeUiState>(TakeUiState.Init)
    val uiState = _uiState.asStateFlow()

    fun loadTodayMedicines(dayWeekDay: WeekDay) {
        getTodayMedicineUseCase.invoke(dayWeekDay) { medicineAtTimes ->
            _uiState.value = TakeUiState.GetTodayMedicinesSuccess(medicineAtTimes)
        }
    }

    fun loadMyMedicines() {
        getMyMedicinesUseCase.invoke { myMedicines ->
            _uiState.value = TakeUiState.GetMyMedicinesSuccess(myMedicines)
        }
    }
}