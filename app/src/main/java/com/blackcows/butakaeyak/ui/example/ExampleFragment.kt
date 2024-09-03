package com.blackcows.butakaeyak.ui.example

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ExampleFragment: Fragment() {

    private val exampleViewModel: ExampleViewModel by activityViewModels()
    private lateinit var userName: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            exampleViewModel.uiState.collectLatest {
                when(it) {
                    is UserUiState.Init -> {
                        //로딩바, 로딩 서클 시작
                    }

                    is UserUiState.GetUserNameSuccess -> {
                        userName = it.userName
                    }

                    is UserUiState.Failure -> {
                        Toast.makeText(requireContext(), "실패", Toast.LENGTH_SHORT).show()
                    }

                    is UserUiState.SaveUserSuccess -> {

                    }

                    is UserUiState.DeleteUserSuccess -> {

                    }

                    is UserUiState.Loading -> {
                        //로딩바, 로딩 서클 끝
                    }
                }
            }
        }
    }
}