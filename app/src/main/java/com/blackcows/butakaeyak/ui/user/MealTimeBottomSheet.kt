package com.blackcows.butakaeyak.ui.user

import android.app.Dialog
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.blackcows.butakaeyak.databinding.DialogMealTimeBinding
import com.blackcows.butakaeyak.ui.take.TimePickerDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MealTimeBottomSheet : BottomSheetDialogFragment() {

    private var _binding: DialogMealTimeBinding? = null
    private val binding get() = _binding!!

    private lateinit var mypageViewModel: MypageViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = super.onCreateDialog(savedInstanceState)

        // 커스텀 레이아웃을 인플레이트
        _binding = DialogMealTimeBinding.inflate(requireActivity().layoutInflater)
        builder.setContentView(binding.root)

        // 확인 버튼
        binding.btnMealSave.setOnClickListener {
            // TODO : 저장 로직 구현 -> 뷰모델 연결
            val list = mutableListOf<String>().apply{
                add(binding.breakfastTimeTextView.text.toString())
                add(binding.lunchTimeTextView.text.toString())
                add(binding.dinnerTimeTextView.text.toString())
            }
            mypageViewModel.saveDefultAlams(list)
            dialog?.dismiss()
        }

        //취소 버튼
        binding.btnMealCancel.setOnClickListener {
            dialog?.dismiss()
        }

        val setBreakfast = binding.cvBreakfastTime
        val setLunch = binding.cvLunchtTime
        val setDinner = binding.cvDinnerTime

        setBreakfast.setOnClickListener {
            showTimePickerDialog(binding.breakfastTimeTextView)
        }

        setLunch.setOnClickListener {
            showTimePickerDialog(binding.lunchTimeTextView)
        }

        setDinner.setOnClickListener {
            showTimePickerDialog(binding.dinnerTimeTextView)
        }

        return builder
    }

    private fun showTimePickerDialog(textView: TextView) {
        val timePickerDialog = TimePickerDialog(requireContext(), textView)
        timePickerDialog.show()
    }
}