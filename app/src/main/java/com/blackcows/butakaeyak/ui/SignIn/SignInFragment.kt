package com.blackcows.butakaeyak.ui.SignIn

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.blackcows.butakaeyak.BuildConfig
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.databinding.FragmentSignInBinding
import com.blackcows.butakaeyak.domain.repo.LocalRepository
import com.blackcows.butakaeyak.domain.repo.UserRepository
import com.blackcows.butakaeyak.ui.navigation.MainNavigation
import com.blackcows.butakaeyak.ui.viewmodels.UserViewModel
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private val TAG = "SignIn"

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var localRepository: LocalRepository


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        KakaoSdk.init(requireContext(), BuildConfig.NATIVE_APP_KEY)

        binding.ivBack.setOnClickListener {
            MainNavigation.popCurrentFragment()
        }
        // ViewModel 초기화
        val viewModel: UserViewModel by viewModels()

        // 카카오 로그인 버튼 클릭 리스너
        binding.ivKakaoLogin.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val result = suspendCoroutine<Pair<OAuthToken?, Throwable?>> { continuation ->
                    if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
                        UserApiClient.instance.loginWithKakaoTalk(requireActivity()) { token, e ->
                            if (e != null) {
                                if (e is ClientError && e.reason == ClientErrorCause.Cancelled) {
                                    return@loginWithKakaoTalk
                                }
                                UserApiClient.instance.loginWithKakaoAccount(requireActivity()) { token, e ->
                                    continuation.resume(Pair(token, e))
                                }
                            } else {
                                continuation.resume(Pair(token, null))
                            }
                        }
                    } else {
                        UserApiClient.instance.loginWithKakaoAccount(requireActivity()) { token, e ->
                            continuation.resume(Pair(token, e))
                        }
                    }
                }


                // 로그인 결과 처리
                withContext(Dispatchers.Main) {
                    if (result.second == null) {
                        // 로그인 성공 시 사용자 정보 가져오기
                        viewModel.signUpWithKakaoAndLogin()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "로그인 실패: ${result.second?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}
