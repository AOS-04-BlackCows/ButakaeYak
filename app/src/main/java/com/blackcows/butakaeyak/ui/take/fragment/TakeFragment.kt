package com.blackcows.butakaeyak.ui.take.fragment

import android.app.AlertDialog
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
import com.blackcows.butakaeyak.MainViewModel
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.data.toKorean
import com.blackcows.butakaeyak.databinding.FragmentTakeBinding
import com.blackcows.butakaeyak.ui.example.UserUiState
import com.blackcows.butakaeyak.ui.navigation.MainNavigation
import com.blackcows.butakaeyak.ui.navigation.TabTag
import com.blackcows.butakaeyak.ui.take.TakeUiState
import com.blackcows.butakaeyak.ui.take.adapter.MyMedicinesRvAdapter
import com.blackcows.butakaeyak.ui.take.adapter.TakeRvDecorator
import com.blackcows.butakaeyak.ui.take.adapter.TodayMedicineRvAdapter
import com.google.api.Distribution.BucketOptions.Linear
import io.ktor.util.date.WeekDay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.junit.internal.runners.statements.Fail
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import com.blackcows.butakaeyak.ui.take.adapter.TakeAdapter
import com.blackcows.butakaeyak.ui.take.data.MedicineAtTime

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
        WeekDay.from((calendar.get(Calendar.DAY_OF_WEEK)+5)%7)
    }

    //private val myTakeViewModel: MyTakeViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    private val todayMedicinesAdapter = TodayMedicineRvAdapter()
    private val myMedicinesAdapter = MyMedicinesRvAdapter() {
        AlertDialog.Builder(requireContext())
            .setTitle("그만 복용하실건가요?")
            .setPositiveButton("네") { d, which ->
                mainViewModel.cancelMyMedicine(it.medicine.id!!)
            }.setNegativeButton("아니요") { d, _ ->
                d.dismiss()
            }
            .show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var adapter: TakeAdapter

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initView() = with(binding) {
        todayDateTv.text = "${todayDate.get(Calendar.YEAR)}년 ${todayDate.time.toKorean()}"

        mainViewModel.getMyMedicineList()
        mainViewModel.myMedicines.observe(viewLifecycleOwner) {
            myMedicinesAdapter.submitList(it.toList())
            updateTodayMedicineList()
        }

        noTodayGuideGoBtn.setOnClickListener {
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

    private fun updateTodayMedicineList() {
        val result = mutableListOf<MedicineAtTime>()
        val timeToMedicines = mutableMapOf<String, MutableList<Medicine>>()

        mainViewModel.myMedicines.value!!.forEach {  myMedicine ->
            val todayPlans = myMedicine.alarms
            todayPlans.forEach { time ->
                timeToMedicines
                    .getOrPut(time) { mutableListOf() }
                    .add(myMedicine.medicine)
            }
        }
        timeToMedicines.forEach { (k, v) ->
            if(k.isNotEmpty()) {
                result.add(MedicineAtTime(todayWeekDay, k, v))
            }
        }

        Log.d(TAG, "ToDayMedicine list size: ${result.size}")

        if(result.size != 0) {
            binding.noTodayGuideBox.visibility = View.GONE
            binding.noMyMedicineTv.visibility = View.GONE
            todayMedicinesAdapter.submitList(result)
        } else {
            binding.noTodayGuideBox.visibility = View.VISIBLE
            binding.noMyMedicineTv.visibility = View.VISIBLE
        }

    }
}