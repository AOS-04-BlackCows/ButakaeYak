package com.blackcows.butakaeyak.ui.take.fragment

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.blackcows.butakaeyak.MainViewModel
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.data.models.MedicineGroup
import com.blackcows.butakaeyak.data.models.MedicineGroupRequest
import com.blackcows.butakaeyak.data.models.MedicineGroupResponse
import com.blackcows.butakaeyak.databinding.BottomsheetCalendarBinding
import com.blackcows.butakaeyak.databinding.BottomsheetRepeatCycleBinding
import com.blackcows.butakaeyak.databinding.FragmentCycleBinding
import com.blackcows.butakaeyak.ui.navigation.FragmentTag
import com.blackcows.butakaeyak.ui.navigation.MainNavigation
import com.blackcows.butakaeyak.ui.take.AlarmReceiver
import com.blackcows.butakaeyak.ui.take.TakeAddViewModel
import com.blackcows.butakaeyak.ui.take.TimePickerDialog
import com.blackcows.butakaeyak.ui.take.adapter.CycleAdapter
import com.blackcows.butakaeyak.ui.take.data.AlarmItem
import com.blackcows.butakaeyak.ui.take.data.MyMedicine
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.time.LocalDate
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
    private val viewModel: TakeAddViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var bottomSheetView: BottomsheetCalendarBinding
    private lateinit var bottomSheetDialog: BottomSheetDialog

    private lateinit var bottomSheetView2: BottomsheetRepeatCycleBinding
    private lateinit var bottomSheetDialog2: BottomSheetDialog

    //뒤로가기 설정
    private val onBackPressed = {
            MainNavigation.popCurrentFragment()
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.move_enter,R.anim.move_exit)
                .remove(this)
                .commitNow()
    }

    private val myMedicine : MyMedicine? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        MainNavigation.hideBottomNavigation(true)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        })

        _binding = FragmentCycleBinding.inflate(inflater, container, false)

        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomSheetView = BottomsheetCalendarBinding.inflate(layoutInflater)
        bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(bottomSheetView.root)

        bottomSheetView2 = BottomsheetRepeatCycleBinding.inflate(layoutInflater)
        bottomSheetDialog2 = BottomSheetDialog(requireContext(), R.style.DialogStyle)
        bottomSheetDialog2.setContentView(bottomSheetView2.root)

        binding.etMedicineGroup.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    btnNext()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.apply {
            ivBack.setOnClickListener {
                onBackPressed()
            }

            adapter = CycleAdapter(requireContext(),alarmList){ itemCount ->
                alarmAddTextUpdate(itemCount)
            }
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())

            clStartDay.setOnClickListener {
                bottomSheetDialog.show()
                    bottomSheetView.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
                        val selectDate = "${year}년 ${month + 1}월 ${dayOfMonth}일"
                        binding.tvStartDay.setText(selectDate)

                        //TODO viewModel startDate 값 설정
                        viewModel.startDate = binding.tvStartDay.text.toString()
                        btnNext()
                    }
            }

            val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            binding.clRepeatCycle.setOnClickListener {
                bottomSheetDialog2.show()

                bottomSheetView2.btnNext.setOnClickListener {
                    if(bottomSheetView2.etCustom.text.isNotEmpty()){
                        binding.tvRepeatCycle.text = "${bottomSheetView2.etCustom.text}일"
                        bottomSheetView2.tvCustomCaption.setText("${bottomSheetView2.etCustom.text}일마다 반복합니다.")
                    }
                    inputMethodManager.hideSoftInputFromWindow(bottomSheetView2.root.windowToken,0)
                    bottomSheetDialog2.dismiss()
                    btnNext()
                }

                bottomSheetDialog2.setOnDismissListener {
//                    bottomSheetView2.etCustom.setText("")
                }
            }

            myMedicine?.let {
                if (myMedicine.alarms.isNotEmpty()) {
                    alarmList.clear() // 기존 알람 리스트 초기화

                    for (alarmString in myMedicine.alarms) {
                        // 시간 문자열 포맷이 맞는지 확인
                        val timeParts = alarmString.split(":")
                        if (timeParts.size == 2) {
                            try {
                                val hour = timeParts[0].toInt()
                                val minute = timeParts[1].toInt()
                                val alarmItem = AlarmItem(
                                    timeText = alarmString,
                                    timeInMillis = adapter.getTimeInMillis(hour, minute)
                                )
                                alarmList.add(alarmItem)
                                Log.d("CycleFragment", "Added alarm item: $alarmItem")
                            } catch (e: NumberFormatException) {
                                Log.e("CycleFragment", "Error parsing time: $alarmString", e)
                            }
                        } else {
                            Log.e("CycleFragment", "Invalid time format: $alarmString")
                        }
                    }
                    adapter.notifyDataSetChanged() // 어댑터에 변경사항 알림
                }
            }

            clCycleAlarmAdd.setOnClickListener {
                showCustomTimePickerDialog()
            }
        }
        alarmAddTextUpdate(adapter.itemCount)
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
                alarmAddTextUpdate(adapter.itemCount)

                //TODO viewModel alarms 값 설정
                val alarms: List<String> = alarmList.mapNotNull { it.timeText }
                viewModel.alarms = alarms
            }
        }
        timePickerDialog.show()
    }

    //adapter에 알람 추가
    private fun addAlarmItem(timeText: String) {
        val timeParts = timeText.split(":")
        val hour = timeParts[0].toInt()
        val minute = timeParts[1].toInt()
        val alarmItem = AlarmItem(
            timeText = timeText,
            timeInMillis = adapter.getTimeInMillis(hour, minute)
        )
        adapter.addItem(alarmItem)
    }

    //tvCycleAlarmAdd text 업데이트
    private fun alarmAddTextUpdate(itemCount: Int) {
        if(itemCount>0) {
            binding.tvCycleAlarmAdd.setText("${itemCount}개 설정")
        }
        else{
            binding.tvCycleAlarmAdd.text = ""
        }
        btnNext()
    }

    private fun btnNext(){
        val isMedicineGroup = binding.etMedicineGroup.text.isNotEmpty()
        val isStartDay = binding.tvStartDay.text.isNotEmpty()
        val isRepeatCycle = binding.tvRepeatCycle.text.isNotEmpty()
        val isCycleAlarmAdd = binding.tvCycleAlarmAdd.text.isNotEmpty()

        binding.apply {
            if(isMedicineGroup && isStartDay && isRepeatCycle && isCycleAlarmAdd){
                btnNext.isEnabled = true
                btnNext.setBackgroundResource(R.color.green)
                btnNext.setTextColor(Color.WHITE)

                btnNext.setOnClickListener {
                    val nameGroup = etMedicineGroup.text.toString()
                    val finishDate = LocalDate.parse("2100-12-31").toString()

                    //TODO viewModel 약 그룹이랑 끝나는 날짜 값 설정
                    viewModel.groupName = nameGroup
                    viewModel.finishDate = finishDate

                    //TODO viewModel create, save
//                    val groupCycle = viewModel.createNewMedicineGroupRequest()
//                    viewModel.saveGroup(groupCycle)

                    val repeatCycle = tvRepeatCycle.text.toString()
                    val selectDate = tvStartDay.text.toString()
                    val dateFormat = dateFormatText(selectDate)
                    val startDate = dateFormat
                    Log.d("startDate","${startDate}")

                    setAlarmForAllItems(repeatCycle,startDate)

                    parentFragmentManager.beginTransaction()
                        .remove(this@CycleFragment)
                        .commit()

                    val newMyMedicine = myMedicine?.copy(
                        alarms = alarmList.map {
                            it.timeText!!
                        }
                    )

                    newMyMedicine?.let { it1 -> mainViewModel.addToMyMedicineList(it1) }
                    MainNavigation.popCurrentFragment()
                }
            }
            else{
                btnNext.isEnabled = false
                btnNext.setBackgroundResource(R.color.gray)
                btnNext.setTextColor(Color.DKGRAY)
            }
        }
    }

    private fun setAlarmForAllItems(repeatType:String, startDate : Long) {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val alarmList = adapter.getAlarmList()

        for (alarm in alarmList) {
            Log.d("timeInMillis","${alarm.timeInMillis}")
            Log.d("requestCode","${alarm.requestCode}")
            val intent = Intent(requireContext(), AlarmReceiver::class.java)

            intent.putExtra("NOTIFICATION_ID", alarm.requestCode)
            intent.putExtra("NOTIFICATION_TITLE","${binding.etMedicineGroup.text}")
            intent.putExtra("NOTIFICATION_CONTENT","약 먹을 시간입니다.")

            val pendingIntent = PendingIntent.getBroadcast(
                context, alarm.requestCode, intent, PendingIntent.FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                // Android 12 이상에서 정확한 알람 설정

                Log.d("buildTest", "okay")
                if (alarmManager?.canScheduleExactAlarms() == true){
                try {
                    val alarmTime = alarm.timeInMillis
                    //선택한 날짜 + 알람 설정한 시간 더하는 변수
                    val adjustedTimeInMillis = startDate + (alarmTime % (24 * 60 * 60 * 1000)) + (9 * 60 * 60 * 1000)

                    Log.d("AlarmTest", "Adjusted Time In Millis: $adjustedTimeInMillis")

                    //반복 주기에 따른 알람 설정 추가
                    scheduleNextAlarm(repeatType, alarmManager, pendingIntent, adjustedTimeInMillis)

                } catch (e: SecurityException) {
                    Toast.makeText(context, "알림 설정에 실패했습니다. 권한을 확인해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
            }
            else{
                Log.d("alarmTest","if문 false뜸")
            }
        }

        Toast.makeText(context, "알림이 설정되었습니다.", Toast.LENGTH_SHORT).show()
    }

    //년,월,일 추출하는 메소드
    fun dateFormatText(dateText: String): Long {
        val year = dateText.substringBefore("년").trim().toInt()
        val month = dateText.substringAfter("년").substringBefore("월").trim().toInt() - 1 // Calendar는 0부터 시작
        val day = dateText.substringAfter("월").substringBefore("일").trim().toInt()

        // Calendar 날짜 설정
        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, day)
            set(Calendar.HOUR_OF_DAY, 0) // 시간 설정
            set(Calendar.MINUTE, 0) // 분 설정
            set(Calendar.SECOND, 0) // 초 설정
            set(Calendar.MILLISECOND, 0) // 밀리초 설정
        }

        return calendar.timeInMillis // 밀리초 단위로 반환
    }

    private fun scheduleNextAlarm(repeatType: String, alarmManager: AlarmManager, pendingIntent: PendingIntent, currentTimeMillis: Long) {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = currentTimeMillis

        val repeatInterval: Long
        Log.d("AlarmDebug", "Repeat Type: $repeatType")

        when (repeatType) {
            "1일", "2일", "3일", "4일", "5일", "6일", "7일" -> {
                // 일 단위 반복
                repeatInterval = repeatType.replace("일", "").toLong()
                Log.d("AlarmDebug2", "Repeat Type: $repeatInterval")
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    repeatInterval * AlarmManager.INTERVAL_DAY,
                    pendingIntent
                )
            }
        }

        // 반복되는 알람 설정
        val nextAlarmClock = AlarmManager.AlarmClockInfo(calendar.timeInMillis, pendingIntent)
        alarmManager.setAlarmClock(nextAlarmClock, pendingIntent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
            MainNavigation.hideBottomNavigation(false)
        _binding = null
    }

    companion object {
        private const val MEDICINE_DATA = "medicine_data"
        private const val FRAGMENT_TAG = "FRAGMENT_TAG"

        @JvmStatic
        fun newInstance(medicine: Medicine, fragmentTag: FragmentTag) =
            CycleFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(MEDICINE_DATA, medicine)
                    putString(FRAGMENT_TAG, fragmentTag.name)
                }
            }

        @JvmStatic
        fun newInstance() = CycleFragment()
    }
}