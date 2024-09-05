package com.blackcows.butakaeyak.ui.home

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.models.SearchCategory
import com.blackcows.butakaeyak.data.models.SearchCategoryDataSource
import com.blackcows.butakaeyak.databinding.BottomsheetSearchfilterBinding
import com.blackcows.butakaeyak.databinding.FragmentSearchBinding
import com.blackcows.butakaeyak.databinding.ItemSearchfilterBinding
import com.blackcows.butakaeyak.ui.home.adapter.HomeViewPager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {


    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPager: ViewPager2

    private val homeViewModel: HomeViewModel by activityViewModels()

    private object homeData{
        var userEffectList = mutableListOf<Pair<String,String>>()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val viewpager = binding.searchVp
        homeViewModel.text.observe(viewLifecycleOwner) {
            viewpager.currentItem
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

        viewPager = binding.searchVp
        binding.searchVp.adapter = HomeViewPager(this@HomeFragment)

        TabLayoutMediator(binding.searchLoTab, binding.searchVp) { tab, position ->
            tab.text = HomeViewPager(this).pageTag[position]
        }.attach()

        binding.searchBtnSearch.setOnClickListener {
            val query = binding.searchEtSearchtext.text.toString()
            binding.searchLoProgressContainer.visibility = View.VISIBLE
            homeViewModel.searchMedicinesWithName(query)
            /*todo : 검색 완료시 프로그래스바 사라지게
               검색 클릭시 에니메이션
               검색 클릭시 키보드 내려가게
               검색전 횡한화면 채우기*/
            Handler(Looper.getMainLooper()).postDelayed({
                binding.searchLoProgressContainer.visibility = View.GONE
            },5000)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initView(){
        val bottomSheetView = BottomsheetSearchfilterBinding.inflate(layoutInflater)
        val bottomSheetDialog = BottomSheetDialog(requireContext())

        bottomSheetDialog.setContentView(bottomSheetView.root)
        // 카테고리 클릭 시 변경
        val categoryArr = arrayOf(
            binding.searchCategory1, binding.searchCategory2, binding.searchCategory3, binding.searchCategory4,
            binding.searchCategory5, binding.searchCategory6, binding.searchCategory7, binding.searchCategory8,
        )
        for (i in categoryArr) {
            i.setOnCheckedChangeListener { chip, isChecked ->
                bottomSheetView.effectList.removeAllViews()
                if (isChecked){
                    bottomSheetView.effectList.addView(clickedCategory(i,i.text.toString()))
                    bottomSheetDialog.show()
                }else{
                    bottomSheetDialog.hide()
                }
            }
        }
    }
    private fun clickedCategory (view: View, title: String) : View {
        val filterItem = ItemSearchfilterBinding.inflate(layoutInflater)
        filterItem.effectTitle.text = title
        filterItem.effectChipgroup.setChipSpacing(1)
        val selectStatus: SearchCategory = when (view) {
            binding.searchCategory1 -> SearchCategory.HEAD
            binding.searchCategory2 -> SearchCategory.FACE
            binding.searchCategory3 -> SearchCategory.NECK
            binding.searchCategory4 -> SearchCategory.CHEST_THORAX
            binding.searchCategory5 -> SearchCategory.ABDOMEN
            binding.searchCategory6 -> SearchCategory.BACK_WAIST
            binding.searchCategory7 -> SearchCategory.LEGS_FEET
            binding.searchCategory8 -> SearchCategory.SKIN
            else -> SearchCategory.HEAD
        }
        val keyWords = SearchCategoryDataSource.getSearchSubCategory(selectStatus)
        for ( k in keyWords){
            filterItem.effectChipgroup.addView(Chip(this.context).apply {
                text = k.name
                setChipDrawable(ChipDrawable.createFromAttributes(context, null, 0, R.style.CustomChipStyle_On))
                with(this) {
                    chipStrokeWidth = 0.0f
                    setOnClickListener {
                        var str = ""
                        if (isChecked){
                            homeData.userEffectList.add(Pair(title,k.name))
                        }else{
                            homeData.userEffectList.remove(Pair(title,k.name))
                        }
                        for ((index,i) in homeData.userEffectList.withIndex()){
                            Log.d("누른거","모음 ${homeData.userEffectList}, ${i.first} : ${i.second} [${index}] ")
                            if (index != 0) str += ", "
                            str += i.second
                        }
                        binding.searchEtSearchtext.setText(str)
                        Log.d("chip누른다!!","이거 누름 ${k.name}")
                    }
                }
            })
        }
        return filterItem.root
    }
}