package com.blackcows.butakaeyak.ui.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.databinding.FragmentUserBinding
import com.blackcows.butakaeyak.ui.SignIn.SignInFragment
import com.blackcows.butakaeyak.ui.navigation.FragmentTag
import com.blackcows.butakaeyak.ui.navigation.MainNavigation
import com.blackcows.butakaeyak.ui.take.fragment.OpenAPIFragment
import com.blackcows.butakaeyak.ui.take.fragment.TermsFragment
import com.blackcows.butakaeyak.ui.viewmodels.UserViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserFragment : Fragment() {

    private val TAG = "UserFragment"

    //binding 설정
    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObserver()

        // 서비스 이용 약관
        binding.cvServices.setOnClickListener {
            MainNavigation.addFragment(TermsFragment(), FragmentTag.TermsFragment)
        }

        // 오픈 소스
        binding.cvOpenAPI.setOnClickListener {
            MainNavigation.addFragment(OpenAPIFragment(), FragmentTag.OpenAPIFragment)
        }

        // 아침/점심/저녁 시간 설정
        binding.cvSetTime.setOnClickListener {
            val mealTimeBottomSheet = MealTimeBottomSheet()
            mealTimeBottomSheet.show(childFragmentManager, "MealTimeDialog")
        }

        // 로그아웃 콜백 구현
        binding.logout.setOnClickListener {
            if (userViewModel.user.value == null) {
                Log.d(TAG, "user == null")
            }
            Log.d(TAG, "로그아웃 버튼 클릭!")
            userViewModel.logout {

                Toast.makeText(requireContext(), "로그아웃이 되었습니다!!", Toast.LENGTH_SHORT).show()

                binding.loggedInLayout.visibility = View.GONE
                binding.notLoggedInLayout.visibility = View.VISIBLE
            }
        }
    }

    private fun setObserver() {
        userViewModel.user.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                binding.loggedInLayout.visibility = View.VISIBLE
                binding.notLoggedInLayout.visibility = View.GONE

                // 닉네임 및 프로필 이미지 업데이트
                binding.tvName.text = user.name
                Glide.with(this)
                    .load(user.profileUrl ?: R.drawable.account_circle)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(90)))
                    .placeholder(R.drawable.account_circle) // 기본 이미지
                    .into(binding.ivProfile)

            } else {
                // 로그인 되지 않은 경우
                binding.loggedInLayout.visibility = View.GONE
                binding.notLoggedInLayout.visibility = View.VISIBLE

                //로그인 화면으로 이동
                binding.notLoggedInLayout.setOnClickListener {
                    // 로그인 화면으로 이동하는 로직
                    MainNavigation.addFragment(SignInFragment(), FragmentTag.SignInFragment)
                    //userViewModel.signUpWithKakaoAndLogin()
                }
            }
        }
    }
}
