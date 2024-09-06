package com.blackcows.butakaeyak.ui.user

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.blackcows.butakaeyak.MainActivity
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.databinding.FragmentUserBinding
import com.blackcows.butakaeyak.ui.SignIn.SignInActivity
import com.blackcows.butakaeyak.ui.navigation.FragmentTag
import com.blackcows.butakaeyak.ui.navigation.MainNavigation
import com.blackcows.butakaeyak.ui.take.data.MyMedicine
import com.blackcows.butakaeyak.ui.take.fragment.NameFragment

class UserFragment : Fragment() {

    //binding 설정
    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!

    //ViewPager 설정
    private lateinit var userFavoriteViewPagerAdapter: UserFavoriteViewPagerAdapter

    private val MIN_SCALE = 0.85f // 뷰가 몇퍼센트로 줄어들 것인지
    private val MIN_ALPHA = 0.5f // 어두워지는 정도를 나타낸 듯 하다.

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val userViewModel =
            ViewModelProvider(this).get(UserViewModel::class.java)

        val mainActivity = (activity as MainActivity)
        mainActivity.hideBottomNavigation(false)
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //ViewPager 화면 확인용 임시 더미데이터
        val dataList = mutableListOf<test>()
        dataList.add(test(R.drawable.choco,"항히스타민제","피부 질환 완화제"))
        dataList.add(test(R.drawable.choco,"항히스타민제","피부 질환 완화제"))
        userFavoriteViewPagerAdapter = UserFavoriteViewPagerAdapter(dataList)

        binding.apply {
            vp2Favorite.apply{
            adapter = userFavoriteViewPagerAdapter
            setPageTransformer(ZoomOutPageTransformer())
            }

            clMyLogin.setOnClickListener {
                val intent = Intent(requireActivity(),SignInActivity::class.java)
                startActivity(intent)
            }
        }
    }

            //TODO Toggle 클릭 시 NameFragment로 이동하게 하기 위함
            fun bind(item: MyMedicine) {
                MainNavigation.addFragment(
                    NameFragment.newInstance(item.medicine), FragmentTag.NameFragment
                )
            }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //ViewPager 스와이프 시 화면 애니메이션
    inner class ZoomOutPageTransformer : ViewPager2.PageTransformer {
        override fun transformPage(view: View, position: Float) {
            view.apply {
                val pageWidth = width
                val pageHeight = height
                when {
                    position < -1 -> { // [-Infinity,-1)
                        // This page is way off-screen to the left.
                        alpha = 0f
                    }
                    position <= 1 -> { // [-1,1]
                        // Modify the default slide transition to shrink the page as well
                        val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
                        val vertMargin = pageHeight * (1 - scaleFactor) / 2
                        val horzMargin = pageWidth * (1 - scaleFactor) / 2
                        translationX = if (position < 0) {
                            horzMargin - vertMargin / 2
                        } else {
                            horzMargin + vertMargin / 2
                        }

                        // Scale the page down (between MIN_SCALE and 1)
                        scaleX = scaleFactor
                        scaleY = scaleFactor

                        // Fade the page relative to its size.
                        alpha = (MIN_ALPHA +
                                (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
                    }
                    else -> { // (1,+Infinity]
                        // This page is way off-screen to the right.
                        alpha = 0f
                    }
                }
            }
        }
    }
}