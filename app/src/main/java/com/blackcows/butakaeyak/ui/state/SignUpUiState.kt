package com.blackcows.butakaeyak.ui.state

sealed class SignUpUiState {
    data object Success: SignUpUiState()
    data object UnKnownUserData: SignUpUiState()
    data object  KakaoSignUpFail: SignUpUiState()

    data object Failure: SignUpUiState()
    data object Loading: SignUpUiState()
    data object Init: SignUpUiState ()
}