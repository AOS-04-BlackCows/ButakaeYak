package com.blackcows.butakaeyak.ui.schedule

sealed class ScheduleUiState {
    data object Success: ScheduleUiState()
    data object Loading: ScheduleUiState()
    data object Init: ScheduleUiState()
}