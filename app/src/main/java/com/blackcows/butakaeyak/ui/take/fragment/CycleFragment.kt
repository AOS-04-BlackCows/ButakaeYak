package com.blackcows.butakaeyak.ui.take.fragment

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.databinding.FragmentCycleBinding
import com.blackcows.butakaeyak.ui.home.HomeFragment
import com.blackcows.butakaeyak.ui.take.AlarmReceiver
import com.blackcows.butakaeyak.ui.take.TakeViewModel
import com.blackcows.butakaeyak.ui.take.TimePickerDialog
import com.blackcows.butakaeyak.ui.take.adapter.CycleAdapter
import com.blackcows.butakaeyak.ui.take.data.AlarmItem
import java.util.Calendar


class CycleFragment : Fragment() {

    //binding 설정
    private var _binding: FragmentCycleBinding? = null
    private val binding get() = _binding!!

    //adapter 설정
    private lateinit var adapter : CycleAdapter

    //data class
    private val alarmList = mutableListOf<AlarmItem>()

    //viewModel 설정
    private val viewModel: TakeViewModel by activityViewModels()

    //bundle에서 medicine 가져오기
    private val medicine: Medicine by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(MEDICINE_DATA, Medicine::class.java)!!
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(MEDICINE_DATA)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

            }
        })
        _binding = FragmentCycleBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("CycleFragment", "${medicine.name}, ${medicine.id}")

        binding.apply {
            ivBack.setOnClickListener {
//                MainNavigation.popCurrentFragments()
            }
            adapter = CycleAdapter(requireContext(),alarmList)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            adapter.notifyDataSetChanged()
            clCycleAlarmAdd.setOnClickListener {
                showCustomTimePickerDialog()
            }

            //버튼
            handleConfirmButton()

            adapter.setOnDaySelectedListener {
                handleConfirmButton()
            }

            viewModel.getData().observe(viewLifecycleOwner, Observer {
                tvCycleName.text = "약 이름 : ${it}"
            })
            viewModel.getTextData().observe(viewLifecycleOwner, Observer{
                tvCycleForm.text = "약 모형 : " + it
            })
            viewModel.getImageData().observe(viewLifecycleOwner, Observer{
                ivCycleForm.setImageResource(it)
            })

            btnNext.setOnClickListener {
                if(isAnyDaySelected()) {
                    // TakeAddFragment를 종료
                    requireActivity().supportFragmentManager.popBackStack()

                // HomeFragment로 이동
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.fragment_container_view,
                            HomeFragment()
                        )
                        .commit()

                    saveAlarmInfo()
                } else {
                    // 요일이 선택되지 않았을 때는 아무 동작도 하지 않음
                    Toast.makeText(requireContext(), "요일을 선택해주세요", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

private fun isAnyDaySelected(): Boolean {
    return adapter.alarmList.any { alarmItem ->
        val viewHolder = binding.recyclerView.findViewHolderForAdapterPosition(adapter.alarmList.indexOf(alarmItem)) as? CycleAdapter.CycleViewHolder
        viewHolder?.let { isAnyDaySelected(it) } ?: false
    }
}

    private fun isAnyDaySelected(viewHolder: CycleAdapter.CycleViewHolder): Boolean {
        return viewHolder.monday.currentTextColor == ContextCompat.getColor(requireContext(), R.color.green) ||
                viewHolder.tuesday.currentTextColor == ContextCompat.getColor(requireContext(), R.color.green) ||
                viewHolder.wednesday.currentTextColor == ContextCompat.getColor(requireContext(), R.color.green) ||
                viewHolder.thursday.currentTextColor == ContextCompat.getColor(requireContext(), R.color.green) ||
                viewHolder.friday.currentTextColor == ContextCompat.getColor(requireContext(), R.color.green) ||
                viewHolder.saturday.currentTextColor == ContextCompat.getColor(requireContext(), R.color.green) ||
                viewHolder.sunday.currentTextColor == ContextCompat.getColor(requireContext(), R.color.green)
    }

    private fun saveAlarmInfo() {
        adapter.alarmList.forEach { alarmItem ->
            val viewHolder = binding.recyclerView.findViewHolderForAdapterPosition(adapter.alarmList.indexOf(alarmItem)) as? CycleAdapter.CycleViewHolder
            viewHolder?.let {
                if (isAnyDaySelected(it)) {
                    setLocalAlarm(it.timeText.text.toString())
                }
            }
        }
    }

    private fun setLocalAlarm(timeText: String) {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // 시간 텍스트 (예: "07:30")를 파싱하여 Calendar 객체에 설정
        val timeParts = timeText.split(":")
        val hour = timeParts[0].toInt()
        val minute = timeParts[1].toInt()

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)  // 초는 0으로 설정
            if (before(Calendar.getInstance())) {
                // 설정한 시간이 현재 시간보다 이전이라면, 다음 날로 설정
                add(Calendar.DATE, 1)
            }
        }

        // 알람을 받기 위한 PendingIntent 생성
        val intent = Intent(requireContext(), AlarmReceiver::class.java).apply {
            putExtra("NOTIFICATION_ID", (0..1000).random()) // 고유 ID 생성
            putExtra("NOTIFICATION_TITLE", "${binding.tvCycleName.text}")
            putExtra("NOTIFICATION_CONTENT", "약 먹을 시간입니다.")
        }
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            intent.getIntExtra("NOTIFICATION_ID", 0), // 고유 ID (여러 알람을 구분하려면 다른 ID 사용)
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 알람 설정 (매일 특정 시간에 알람 발생)
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )

        // 사용자에게 알람 설정이 완료되었음을 알려줌
        Toast.makeText(requireContext(), "알람이 설정되었습니다: $timeText", Toast.LENGTH_SHORT).show()
    }

    // 요일이 선택되었는지 확인하여 확인 버튼 활성화 및 배경색 설정
    private fun handleConfirmButton() {
        val isAnyDaySelected = adapter.alarmList.all { alarmItem ->
            val viewHolder = binding.recyclerView.findViewHolderForAdapterPosition(adapter.alarmList.indexOf(alarmItem)) as? CycleAdapter.CycleViewHolder
            viewHolder?.let { isAnyDaySelected(it) } ?: false
        }

        binding.btnNext.isEnabled = isAnyDaySelected

        // 버튼 배경색 변경
        val buttonColor = if (isAnyDaySelected) {
            ContextCompat.getColor(requireContext(), R.color.green)
        } else {
            ContextCompat.getColor(requireContext(), R.color.gray)
        }

        // 버튼 글씨색 변경
        val buttonTextColor = if (isAnyDaySelected) {
            ContextCompat.getColor(requireContext(), R.color.white)
        } else {
            ContextCompat.getColor(requireContext(), R.color.dark_gray)
        }

        binding.btnNext.setBackgroundColor(buttonColor)
        binding.btnNext.setTextColor(buttonTextColor)
    }


    private fun showCustomTimePickerDialog() {
        // text 임시 설정
        val tempTextView = TextView(requireContext())

        // 커스텀 TimePickerDialog 생성 및 보여주기
        val timePickerDialog = TimePickerDialog(requireContext(), tempTextView)
        timePickerDialog.setOnDismissListener {
            val selectedTime = tempTextView.text.toString()
            if (selectedTime.isNotEmpty()) {
                addAlarmItem(selectedTime)
            }
        }
        timePickerDialog.show()
    }

    private fun addAlarmItem(timeText: String) {
        val newAlarm = AlarmItem(
            timeText,
            false, false, false, false,
            false, false, false,
            ContextCompat.getColor(requireContext(), R.color.green),
            ContextCompat.getColor(requireContext(), R.color.dark_gray)
        )
        alarmList.add(newAlarm)
        adapter.notifyItemInserted(alarmList.size - 1)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val MEDICINE_DATA = "medicine_data"

        @JvmStatic
        fun newInstance(medicine: Medicine) =
            CycleFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(MEDICINE_DATA, medicine)
                }
            }
    }
}