package com.blackcows.butakaeyak.ui.example

import androidx.lifecycle.ViewModel
import com.blackcows.butakaeyak.domain.example.GetUserNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ExampleViewModel @Inject constructor(
    private val getUserNameUseCase: GetUserNameUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow<UserUiState>(UserUiState.Init)
    val uiState = _uiState.asStateFlow()

    fun getUserName(id: String) {
        getUserNameUseCase.invoke(id) { userName ->
            if(userName.isNotEmpty()) {
                _uiState.value = UserUiState.GetUserNameSuccess(userName)
            } else {
                _uiState.value = UserUiState.Failure
            }
        }
    }
}