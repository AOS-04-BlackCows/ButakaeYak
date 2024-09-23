package com.blackcows.butakaeyak.ui.take.fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.blackcows.butakaeyak.MainActivity
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.databinding.FragmentTakeAddBinding
import com.blackcows.butakaeyak.ui.navigation.FragmentTag
import com.blackcows.butakaeyak.ui.navigation.MainNavigation

class TakeAddFragment : Fragment() {

    //binding 설정
    private var _binding: FragmentTakeAddBinding? = null
    private val binding get() = _binding!!

    //viewPager 설정
    private var viewPager : ViewPager2? = null

    //bundle에서 medicine 가져오기
//    private val medicine: Medicine? by lazy {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            arguments?.getParcelable(MEDICINE_DATA, Medicine::class.java)
//        } else {
//            @Suppress("DEPRECATION")
//            arguments?.getParcelable(MEDICINE_DATA)
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTakeAddBinding.inflate(layoutInflater,container,false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MainNavigation.hideBottomNavigation(true)

//        medicine?.let {
//            childFragmentManager.beginTransaction().add(
//                R.id.fragment_container, NameFragment.newInstance(it)
//            ).commitNow()
//        } ?: run {
//            Log.e("TakeAddFragment", "Medicine data is missing")
            childFragmentManager.beginTransaction().add(
                R.id.fragment_container, NameFragment()
            ).commitNow()
//        }
    }

    companion object {
        private const val MEDICINE_DATA = "medicine_data"

        @JvmStatic
        fun newInstance(medicine: Medicine) =
            TakeAddFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(MEDICINE_DATA, medicine)
                }
            }

        @JvmStatic
        fun newInstance() = TakeAddFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("TakeAddFragment", "OnDestroyView")
        MainNavigation.hideBottomNavigation(false)
        _binding = null
    }

}