package com.blackcows.butakaeyak.ui.search

import com.blackcows.butakaeyak.data.models.Medicine

sealed class SearchUiState {
    data class SearchMedicinesSuccess(val medicines: List<Medicine>): SearchUiState()

    data object Loading: SearchUiState()
    data object Init: SearchUiState()
}