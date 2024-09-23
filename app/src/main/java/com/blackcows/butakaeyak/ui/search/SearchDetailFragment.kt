package com.blackcows.butakaeyak.ui.search

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.databinding.DialogSearchDetailBinding
import com.blackcows.butakaeyak.ui.navigation.MainNavigation
import com.bumptech.glide.Glide

class SearchDetailFragment () : Fragment(){
    // 뷰 바인딩 정의
    private var _binding: DialogSearchDetailBinding? = null
    private val binding get() = _binding!!

    private val onBackPressedCallback = object: OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {

        }
    }

    //bundle에서 medicine 가져오기
    private val medicine: Medicine by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(SearchDetailFragment.MEDICINE_DATA, Medicine::class.java)!!
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(SearchDetailFragment.MEDICINE_DATA)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogSearchDetailBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        with(binding) {
            Glide.with(root).load(medicine.imageUrl?: R.drawable.medicine).into(binding.detailIvMedicine)

            detailTvName.text = medicine.name
            detailTvEnterprise.text = medicine.enterprise
            detailTvEffect.text = medicine.effect
            detailTvInstructions.text = medicine.instructions
            detailTvWarning.text = medicine.warning
            detailTvCaution.text = medicine.caution
            detailTvInteraction.text = medicine.interaction
            detailTvSideEffect.text = medicine.sideEffect
            detailTvStoringMethod.text = medicine.storingMethod

            detailBtnBack.setOnClickListener {
                MainNavigation.popCurrentFragment()
            }

            root.setOnClickListener {
                Log.d("SearchDetail", "Tab!!")
                MainNavigation.popCurrentFragment()
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val MEDICINE_DATA = "medicine_data"

        @JvmStatic
        fun newInstance(medicine: Medicine) =
            SearchDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(MEDICINE_DATA, medicine)
                }
            }
    }
}