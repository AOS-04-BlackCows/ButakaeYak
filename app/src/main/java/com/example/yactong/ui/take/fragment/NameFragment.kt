package com.example.yactong.ui.take.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.yactong.R
import com.example.yactong.databinding.FragmentNameBinding
import com.example.yactong.ui.take.TakeActivity
import com.example.yactong.ui.take.TakeAddActivity
import com.example.yactong.ui.take.TakeViewModel

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
        _binding = FragmentNameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.ivDelete?.setOnClickListener {
            binding.etMedicineSearch.text = null
        }
        binding?.ivBack?.setOnClickListener {
            //NameFragment는 ViewPager2 안에 감싸져 있는 Fragment
            //ViewPager2의 layout은 TakeAddFragment에 있음
            //Fragment layout은 MainActivity 한 곳에만 있음
//            val takeFragment = TakeFragment()
//
//            requireActivity().supportFragmentManager.beginTransaction()
//                .replace(R.id.nav_host_fragment_activity_main, takeFragment)
//                .addToBackStack(null)
//                .commit()
//            findNavController().navigate(R.id.)
        }

        binding?.etMedicineSearch?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.apply{
                    if(etMedicineSearch.length() > 0){
                        btnNext.apply{
                            isEnabled = true
                            setBackgroundResource(R.drawable.user_cl_bg_green)
                            setTextColor(Color.WHITE)
                            setOnClickListener {
                                viewModel.moveToNextPage()
//                                (requireParentFragment() as TakeAddFragment).moveToNextPage()
                                viewModel.updateItem(etMedicineSearch.text.toString())
                            }
                        }
                    }
                    else{
                        btnNext.apply{
                            isEnabled = false
                            setBackgroundResource(R.drawable.user_cl_bg_gray)
                            setTextColor(Color.DKGRAY)
                        }
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        //TODO 문제 코드
        //키보드 위에 버튼 올리는 코드
     /*   binding?.root?.viewTreeObserver?.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            private var isKeyboardShown = false

            override fun onGlobalLayout() {
                    val r = Rect()
                    binding?.root?.getWindowVisibleDisplayFrame(r)
                    val heightDiff = binding.root.bottom - r.bottom + 80
                    isKeyboardShown = heightDiff > 100
                    val constraintSet = ConstraintSet()
                    constraintSet.clone(binding.root)
                    constraintSet.setMargin(
                        R.id.btn_next,
                        ConstraintSet.BOTTOM,
                        if (isKeyboardShown) heightDiff else 20
                    )
                    constraintSet.applyTo(binding.root)
                }
        })*/

        //editText 클릭 시 키보드 올리는 코드
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        binding?.apply{
                inputMethodManager.showSoftInput(etMedicineSearch, 0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}