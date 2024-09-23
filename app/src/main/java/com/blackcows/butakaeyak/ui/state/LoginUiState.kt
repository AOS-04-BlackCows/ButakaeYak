package com.blackcows.butakaeyak.ui.state

import com.blackcows.butakaeyak.data.models.User

sealed class LoginUiState {
    data object Success: LoginUiState()
    data object UnKnownUserData: LoginUiState()
    data object NotFoundAutoLoginData: LoginUiState()

    data object Failure: LoginUiState()
    data object Loading: LoginUiState()
    data object Init: LoginUiState()
}