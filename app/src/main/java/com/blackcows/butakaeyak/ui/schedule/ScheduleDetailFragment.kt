package com.blackcows.butakaeyak.ui.schedule

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.blackcows.butakaeyak.data.models.MedicineGroup
import com.blackcows.butakaeyak.databinding.BottomsheetCalendarBinding
import com.blackcows.butakaeyak.databinding.BottomsheetMedicineGroupBinding
import com.blackcows.butakaeyak.databinding.FragmentScheduleDetailBinding
import com.blackcows.butakaeyak.ui.navigation.MainNavigation
import com.blackcows.butakaeyak.ui.note.recycler.NoteRvDecoration
import com.blackcows.butakaeyak.ui.schedule.recycler.ScheduleRvAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate


class ScheduleDetailFragment : Fragment() {

    private var _binding: FragmentScheduleDetailBinding? = null
    private val binding get() = _binding!!

    private val scheduleViewModel: ScheduleViewModel by activityViewModels()

    private lateinit var medicineGroupBottomSheet: BottomsheetMedicineGroupBinding
    private lateinit var medicineGroupBottomSheetDialog: BottomSheetDialog

    private lateinit var calenderBottomSheet: BottomsheetCalendarBinding
    private lateinit var calenderBottomSheetDialog: BottomSheetDialog

    private val userId by lazy {
        Log.d("ScheduleFragment", "detail: userId: ${arguments?.getString(FRIEND_ID_DATA)!!}")
        arguments?.getString(FRIEND_ID_DATA)!!
    }
    private val isMine by lazy {
        Log.d("ScheduleFragment", "detail: userId: ${arguments?.getString(FRIEND_ID_DATA)!!}")
        arguments?.getBoolean(IS_MINE)!!
    }

    private var selectedMedicineGroup: MedicineGroup? = null
    private lateinit var scheduleRvAdapter: ScheduleRvAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScheduleDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            scheduleViewModel.uiState.collectLatest {
                when(it) {
                    is ScheduleUiState.Loading -> {
                        MainNavigation.showLoadingBar()
                    }

                    is ScheduleUiState.Success -> {
                        MainNavigation.disableLoadingBar()
                    }

                    is ScheduleUiState.Init -> {

                    }
                }
            }
        }

        initMedicineGroupBottomSheet()
        initCalendarBottomSheet()

        with(binding) {
            val today = LocalDate.now()

            dateTv.text = today.toString().replace("-", ".")

            scheduleRvAdapter = ScheduleRvAdapter(!isMine, object: ScheduleRvAdapter.ClickListener {
                override fun onModifyClick(medicineGroup: MedicineGroup) {
                    selectedMedicineGroup = medicineGroup
                    medicineGroupBottomSheetDialog.show()
                }
                override fun onCheckClick(medicineGroup: MedicineGroup, taken: Boolean, alarm: String) {
                    scheduleViewModel.checkTakenMedicineGroup(medicineGroup, taken, alarm)
                }
            })

            todayAlarmRv.run {
                adapter = scheduleRvAdapter
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(NoteRvDecoration.getLinearDecoSimpleItem())
            }

            openCalendarBtn.setOnClickListener {
                calenderBottomSheetDialog.show()
            }
        }



        scheduleViewModel.dateToMedicineGroup.observe(viewLifecycleOwner) {
            scheduleRvAdapter.submitList(scheduleViewModel.changeToTimeToGroup())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val FRIEND_ID_DATA = "FRIEND_ID_DATA"
        private const val IS_MINE = "IS_MINE"

        @JvmStatic
        fun newInstance(friendId: String, isMine: Boolean) =
            ScheduleDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(FRIEND_ID_DATA, friendId)
                    putBoolean(IS_MINE, isMine)
                }
            }
    }

    private fun initMedicineGroupBottomSheet() {
        val removeDialog
                = AlertDialog.Builder(requireContext())
            .setTitle("정말로 지우시겠습니까?")
            .setPositiveButton("네") { dialog, _ ->
                scheduleViewModel.removeMedicineGroup(selectedMedicineGroup!!)
                selectedMedicineGroup = null
                dialog.dismiss()
            }.setNegativeButton("아니요") { dialog, _ ->
                dialog.dismiss()
            }.setOnDismissListener { medicineGroupBottomSheetDialog.dismiss() }

        medicineGroupBottomSheet = BottomsheetMedicineGroupBinding.inflate(layoutInflater)
        medicineGroupBottomSheetDialog = BottomSheetDialog(requireContext())
        medicineGroupBottomSheetDialog.setContentView(medicineGroupBottomSheet.root)

        medicineGroupBottomSheet.modifyBtn.setOnClickListener {
            //TODO: takeAddViewModel.selectedGroup = selectedGroup
            //  MainNavigation.addFragment(TakeAddFragment(), FragmentTag.TakeAddFragmentInSchedule)
        }
        medicineGroupBottomSheet.removeBtn.setOnClickListener {
            removeDialog.show()
        }
    }
    private fun initCalendarBottomSheet() {
        calenderBottomSheet = BottomsheetCalendarBinding.inflate(layoutInflater)
        calenderBottomSheetDialog = BottomSheetDialog(requireContext())
        calenderBottomSheetDialog.setContentView(calenderBottomSheet.root)
        calenderBottomSheet.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val monthStr = if(month < 10) "0${month+1}"
                            else month.toString()
            val dayOfMonthStr = if(dayOfMonth < 10) "0$dayOfMonth"
                            else dayOfMonth.toString()

            val date = LocalDate.parse("$year-$monthStr-$dayOfMonthStr")
            scheduleViewModel.getDateToMedicineGroup(userId, date)

            binding.dateTv.text = date.toString().replace("-", ".")

            calenderBottomSheetDialog.dismiss()
        }
    }
}