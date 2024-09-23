package com.blackcows.butakaeyak.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blackcows.butakaeyak.data.models.AutoLoginData
import com.blackcows.butakaeyak.data.models.User
import com.blackcows.butakaeyak.domain.repo.LocalUtilsRepository
import com.blackcows.butakaeyak.domain.repo.UserRepository
import com.blackcows.butakaeyak.domain.result.LoginResult
import com.blackcows.butakaeyak.domain.result.SignUpResult
import com.blackcows.butakaeyak.ui.state.LoginUiState
import com.blackcows.butakaeyak.ui.state.SignUpUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val localUtilsRepository: LocalUtilsRepository,
    private val dispatcher: CoroutineDispatcher
): ViewModel() {
    private val _user = MutableLiveData<User?>(null)
    val user get() = _user

    private val _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.Init)
    val loginUiState = _loginUiState.asStateFlow()

    private val _signUpUiState = MutableStateFlow<SignUpUiState>(SignUpUiState.Init)
    val signUpUiState = _signUpUiState.asStateFlow()



    fun autoLogin() {
        viewModelScope.launch {
            _loginUiState.value = LoginUiState.Loading

            val data = localUtilsRepository.getAutoLoginData()
            val result =
                if(data == null) LoginResult.Failure
                else {
                    if(data.isKakao) userRepository.loginWithKakaoId(data.kakaoId.toLong())
                    else userRepository.loginWithId(data.loginId, data.pwd)
                }


            when(result) {
                is LoginResult.Success -> {
                    _user.value = result.user
                    _loginUiState.value = LoginUiState.Success
                }
                is LoginResult.UnknownAccount -> {
                    _loginUiState.value = LoginUiState.UnKnownUserData
                }
                is LoginResult.Failure -> {
                    _loginUiState.value = LoginUiState.NotFoundAutoLoginData
                }
            }
        }
    }

    // 카카오 로그인에 사용
    fun signUpWithKakaoAndLogin() {
        viewModelScope.launch {
            val signUpResult = userRepository.trySignUpWithKakao()
            Log.d("UserViewModel", signUpResult.toString())
            when(signUpResult) {
                is SignUpResult.Success -> {
                    loginWithKakao(signUpResult.user.kakaoId!!.toLong())
                    _signUpUiState.value = SignUpUiState.Success
                }
                is SignUpResult.KakaoSignUpFail -> {
                    _signUpUiState.value = SignUpUiState.KakaoSignUpFail
                }

                is SignUpResult.LoginIdDuplicate -> {
                    val duplicated = Exception("SignUpWithKakao: 카카오로 로그인 시도했는데 LoginIdDuplicated가 반환됨.")
                    Log.w("UserViewModel", duplicated.message!!)

                    _signUpUiState.value = SignUpUiState.Failure
                }

                is SignUpResult.Failure -> {
                    _signUpUiState.value = SignUpUiState.Failure
                }
            }
        }
    }

    private fun loginWithKakao(kakakoId: Long) {
        viewModelScope.launch {
            val result = userRepository.loginWithKakaoId(kakakoId)
            Log.d("UserViewModel: Login", result.toString())
            when(result) {
                is LoginResult.Success -> {
                    _user.value = result.user
                    localUtilsRepository.saveAutoLoginData(
                        AutoLoginData(
                            isKakao = true,
                            loginId = "",
                            pwd = "",
                            kakaoId = result.user.kakaoId!!.toString()
                        )
                    )
                    _loginUiState.value = LoginUiState.Success

                    Log.d("UserViewModel", "User name is ${user.value!!.name}")
                }
                is LoginResult.UnknownAccount -> {
                    _loginUiState.value = LoginUiState.UnKnownUserData
                }
                is LoginResult.Failure -> {
                    _loginUiState.value = LoginUiState.Failure
                }
            }
        }
    }

    fun saveAutoLoginData(autoLoginData: AutoLoginData) {
        localUtilsRepository.saveAutoLoginData(autoLoginData)
    }

    fun logout(onFinished: () -> Unit) {
        viewModelScope.launch {
            _user.value = null
            _loginUiState.value = LoginUiState.Init
            userRepository.logout()
            onFinished()
        }
    }
}