package com.blackcows.butakaeyak.ui.SignIn

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.blackcows.butakaeyak.databinding.FragmentSignInBinding
import com.blackcows.butakaeyak.ui.navigation.MainNavigation
import com.blackcows.butakaeyak.ui.state.LoginUiState
import com.blackcows.butakaeyak.ui.viewmodels.UserViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private val TAG = "SignIn"

    private val userViewModel: UserViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            userViewModel.loginUiState.collectLatest {
                Log.d("SignInFragment: Login", it.toString())
                when(it) {
                    is LoginUiState.Success -> {
                        // 로그인 성공하면 마이페이지로 이동!
                        Toast.makeText(requireContext(), "login: 로그인 성공", Toast.LENGTH_SHORT).show()
                        MainNavigation.popCurrentFragment()
                        MainNavigation.disableLoadingBar()
                    }

                    is LoginUiState.Loading -> {
                        MainNavigation.showLoadingBar()
                    }

                    is LoginUiState.UnKnownUserData -> {
                        Toast.makeText(requireContext(), "login: 언노운", Toast.LENGTH_SHORT).show()
                        MainNavigation.disableLoadingBar()
                    }

                    is LoginUiState.Failure -> {
                        Toast.makeText(requireContext(), "login: 로그인 실패...", Toast.LENGTH_SHORT).show()
                        MainNavigation.disableLoadingBar()
                    }

                    else -> { MainNavigation.disableLoadingBar() }
                }
            }
        }




        binding.ivKakaoLogin.setOnClickListener {
            userViewModel.signUpWithKakaoAndLogin()
        }
        binding.ivBack.setOnClickListener {
            MainNavigation.popCurrentFragment()
        }
    }
}
