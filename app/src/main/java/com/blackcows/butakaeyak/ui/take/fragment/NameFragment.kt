package com.blackcows.butakaeyak.ui.take.fragment

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
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.databinding.FragmentNameBinding
import com.blackcows.butakaeyak.ui.take.TakeActivity
import com.blackcows.butakaeyak.ui.take.TakeAddActivity
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
        _binding = FragmentNameBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivDelete.setOnClickListener {
            binding.etMedicineSearch.text = null
        }

        binding.ivBack.setOnClickListener {
            val intent = Intent(requireContext(), TakeActivity::class.java)
            startActivity(intent)
            requireActivity().overridePendingTransition(R.anim.move_end,R.anim.none)
        }

        binding.etMedicineSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(binding.etMedicineSearch.length() > 0){
                    binding.btnNext.apply{
                        isEnabled = true
                        setBackgroundResource(R.drawable.user_cl_bg_green)
                        setTextColor(Color.WHITE)
                        setOnClickListener {
                            (requireActivity() as TakeAddActivity).moveToNextPage()
                            viewModel.updateItem(binding.etMedicineSearch.text.toString())
                        }
                    }
                }
                else{
                    binding.btnNext.apply{
                        isEnabled = false
                        setBackgroundResource(R.drawable.user_cl_bg_gray)
                        setTextColor(Color.DKGRAY)
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        //키보드 위에 버튼 올리는 코드
        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            private var isKeyboardShown = false

            override fun onGlobalLayout() {
                val r = Rect()
                binding.root.getWindowVisibleDisplayFrame(r)
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
        })

        //editText 클릭 시 키보드 올리는 코드
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        binding.apply{
                inputMethodManager.showSoftInput(etMedicineSearch, 0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}