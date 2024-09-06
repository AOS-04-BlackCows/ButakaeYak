package com.blackcows.butakaeyak.ui.take.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.blackcows.butakaeyak.MainActivity
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.databinding.FragmentNameBinding
import com.blackcows.butakaeyak.ui.navigation.FragmentTag
import com.blackcows.butakaeyak.ui.navigation.MainNavigation
import com.blackcows.butakaeyak.ui.take.TakeViewModel

class NameFragment : Fragment() {

    //binding 설정
    private var _binding: FragmentNameBinding? = null
    private val binding get() = _binding!!

    //viewModel 설정
    private val viewModel: TakeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val mainActivity = activity as MainActivity
        mainActivity.hideBottomNavigation(true)

        _binding = FragmentNameBinding.inflate(inflater, container, false)
        val root: View = binding.root

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewModel.getData().observe(viewLifecycleOwner, Observer {
                etMedicineName.setText(it)
            })
        }

        //companion object
        val medicine: Medicine? = arguments?.getParcelable(MEDICINE_DATA)

        binding.ivDelete.setOnClickListener {
            binding.etMedicineName.text = null
        }

        binding.ivBack.setOnClickListener {
        }

        binding.etMedicineName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.apply{
                    if(etMedicineName.length() > 0){
                        btnNext.apply{
                            isEnabled = true
                            setBackgroundResource(R.color.green)
                            setTextColor(Color.WHITE)
                            setOnClickListener {
                                Log.d("버튼","버튼 눌림")
                                medicine?.let { it1 -> FormFragment.newInstance(it1) }?.let { it2 ->
                                    MainNavigation.addFragment(
                                        it2, FragmentTag.NameFragment
                                    )
                                }
                                viewModel.updateItem(etMedicineName.text.toString())
                            }
                        }
                    }
                    else{
                        btnNext.apply{
                            isEnabled = false
                            setBackgroundResource(R.color.gray)
                            setTextColor(Color.DKGRAY)
                        }
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        //editText 클릭 시 키보드 올리는 코드
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        binding.apply{
                inputMethodManager.showSoftInput(etMedicineName, 0)
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
            NameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(MEDICINE_DATA, medicine)
                }
            }
    }
}