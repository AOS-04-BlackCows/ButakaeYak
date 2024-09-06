package com.blackcows.butakaeyak.ui.take.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.blackcows.butakaeyak.MainActivity
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.databinding.FragmentTakeBinding
import com.blackcows.butakaeyak.domain.take.GetTodayMedicineUseCase
import com.blackcows.butakaeyak.ui.example.UserUiState
import com.blackcows.butakaeyak.ui.navigation.MainNavigation
import com.blackcows.butakaeyak.ui.navigation.TabTag
import com.blackcows.butakaeyak.ui.take.MyTakeViewModel
import com.blackcows.butakaeyak.ui.take.TakeUiState
import com.blackcows.butakaeyak.ui.take.adapter.MyMedicinesRvAdapter
import com.blackcows.butakaeyak.ui.take.adapter.TakeRvDecorator
import com.blackcows.butakaeyak.ui.take.adapter.TodayMedicineRvAdapter
import com.blackcows.butakaeyak.ui.take.toKorean
import com.google.api.Distribution.BucketOptions.Linear
import io.ktor.util.date.WeekDay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.junit.internal.runners.statements.Fail
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import com.blackcows.butakaeyak.ui.take.TakeViewModel
import com.blackcows.butakaeyak.ui.take.adapter.TakeAdapter

class TakeFragment : Fragment() {

    //binding 설정
    private var _binding: FragmentTakeBinding? = null
    private val binding get() = _binding!!

    private val TAG = "TakeFragment"

    private val todayDate = Calendar.getInstance()
    private val todayWeekDay by lazy {
        val calendar = Calendar.getInstance()
            .apply {
            setTime(Date())
        }
        //TODO 오늘 날짜 임시 수정
        WeekDay.from((calendar.get(Calendar.DAY_OF_WEEK)+5)%7)
    }

    private val myTakeViewModel: MyTakeViewModel by activityViewModels()
    private val todayMedicinesAdapter = TodayMedicineRvAdapter()
    private val myMedicinesAdapter = MyMedicinesRvAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var adapter: TakeAdapter

    //viewModel 설정
    private val viewModel: TakeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTakeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initUiState()
    }

    override fun onResume() {
        super.onResume()
        myTakeViewModel.loadTodayMedicines(todayWeekDay)
        myTakeViewModel.loadMyMedicines()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUiState() = lifecycleScope.launch {
        myTakeViewModel.uiState.collectLatest {
            when (it) {
                is TakeUiState.GetTodayMedicinesSuccess -> {
                    if(it.medicineAtTimes.isEmpty()) {
                        binding.noTodayGuideBox.visibility = View.VISIBLE
                    } else {
                        binding.noTodayGuideBox.visibility = View.GONE
                        todayMedicinesAdapter.submitList(it.medicineAtTimes)
                    }
                }

                is TakeUiState.GetMyMedicinesSuccess -> {
                    Log.d(TAG, "isMyMedicineEmpty: ${it.medicines.isEmpty()}")
                    if(it.medicines.isEmpty()) {
                        binding.noMyMedicineTv.visibility = View.VISIBLE
                    } else {
                        binding.noMyMedicineTv.visibility = View.GONE
                        myMedicinesAdapter.submitList(it.medicines)
                    }
                }

                is TakeUiState.Failure -> {
                    Log.d(TAG, "In $TAG, UiState Failed...")
                }

                else -> null
            }
        }
    }

    private fun initView() = with(binding) {
        todayDateTv.text = "${todayDate.get(Calendar.YEAR)}년 ${todayDate.time.toKorean()}"


        noTodayGuideGoBtn.setOnClickListener {
            //TODO: 어디로 가요?
            //  1. 우선 검색화면으로?
            //      추가1) 튜토리얼
            //      추가2)
            MainNavigation.toOtherTab(TabTag.Search)
        }

        todayMedicineRv.run {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = todayMedicinesAdapter
            addItemDecoration(TakeRvDecorator.getLinearDeco())
        }
        myMedicineRv.run {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = myMedicinesAdapter
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    LinearLayoutManager.VERTICAL
                )
            )
        }
    }
}