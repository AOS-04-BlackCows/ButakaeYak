package com.blackcows.butakaeyak.ui.user

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.blackcows.butakaeyak.databinding.DialogMealTimeBinding
import com.blackcows.butakaeyak.ui.take.TimePickerDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.client.content.LocalFileContent

@AndroidEntryPoint
class MealTimeBottomSheet : BottomSheetDialogFragment() {

    private val TAG = "MealTimeBottomSheet"

    private var _binding: DialogMealTimeBinding? = null
    private val binding get() = _binding!!

    private val mypageViewModel: MypageViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = super.onCreateDialog(savedInstanceState)

        // 커스텀 레이아웃을 인플레이트
        _binding = DialogMealTimeBinding.inflate(requireActivity().layoutInflater)
        builder.setContentView(binding.root)

        // viewModel에서 저장된 알람 시간 가져오기
        val getAlarms = mypageViewModel.getDefaultAlarms()
        Log.d(TAG, "getAlarms ${getAlarms}")
        if (getAlarms.isNotEmpty()) {
            Log.d(
                TAG,
                "breakfast : ${getAlarms[0]}, lunch : ${getAlarms[1]}, dinner : ${getAlarms[2]}"
            )
            binding.breakfastTimeTextView.text = getAlarms[0]
            binding.lunchTimeTextView.text = getAlarms[1]
            binding.dinnerTimeTextView.text = getAlarms[2]
        } else {
            Log.d(TAG, "No Alarm set.")
            // 지정된 시간이 없을 경우, 기본 시간을 설정할 수도 있다.
            binding.breakfastTimeTextView.text = "08:00"
            binding.lunchTimeTextView.text = "13:00"
            binding.dinnerTimeTextView.text = "18:00"
        }


        // 확인 버튼
        binding.btnMealSave.setOnClickListener {
            val list = mutableListOf<String>().apply {
                add(binding.breakfastTimeTextView.text.toString())
                add(binding.lunchTimeTextView.text.toString())
                add(binding.dinnerTimeTextView.text.toString())
            }

            Log.d(TAG, "Alarms to save : $list")
            mypageViewModel.saveDefaultAlarms(list)
            dialog?.dismiss()
        }

        //취소 버튼
        binding.btnMealCancel.setOnClickListener {
            dialog?.dismiss()
        }

        val setBreakfast = binding.cvBreakfastTime
        val setLunch = binding.cvLunchTime
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

        Log.d(TAG, "TimePickerDialog : ${textView.text}")
    }
}