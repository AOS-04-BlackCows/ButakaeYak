package com.blackcows.butakaeyak.ui.example


sealed class UserUiState {
    data class GetUserNameSuccess(val userName: String): UserUiState()

    data object SaveUserSuccess: UserUiState()
    data object DeleteUserSuccess: UserUiState()

    data object Failure: UserUiState()
    data object Loading: UserUiState()
    data object Init: UserUiState()
}