package com.blackcows.butakaeyak.ui.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.blackcows.butakaeyak.databinding.FragmentSearchBinding
import com.blackcows.butakaeyak.ui.search.adapter.SearchViewPager
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "SearchFragment_Log"
class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPager: ViewPager2

    private val searchViewModel: SearchViewModel by activityViewModels()

    private var imm : InputMethodManager? =null
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
        imm =  requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val viewpager = binding.searchVp
        searchViewModel.text.observe(viewLifecycleOwner) {
            viewpager.currentItem
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initUiState()

        viewPager = binding.searchVp
        binding.searchVp.adapter = SearchViewPager(this@SearchFragment)

        TabLayoutMediator(binding.searchLoTab, binding.searchVp) { tab, position ->
            tab.text = SearchViewPager(this).pageTag[position]
        }.attach()

        binding.searchBtnSearch.setOnClickListener {
            searchQuery()
        }
        binding.searchEtSearchtext.setOnEditorActionListener { v, actionId, event ->
            Log.d(TAG, "keyCode: ${actionId} -- ${EditorInfo.IME_ACTION_DONE} ")
            if(actionId == EditorInfo.IME_ACTION_DONE){
                searchQuery()
            }

            true
        }
    }
    private fun searchQuery(){
        val query = binding.searchEtSearchtext.text.toString()
        if(query.isEmpty()){
            Toast.makeText(requireContext(), "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show()
        }else{
            binding.searchLoImageblock.visibility = View.GONE
            imm!!.hideSoftInputFromWindow(binding.searchBtnSearch.windowToken, 0)
            searchViewModel.searchMedicinesWithName(query)

            //TODO 키보드 내리기
            val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view?.windowToken ?: null, 0)
        }
    }

    private fun initUiState() {
        lifecycleScope.launch {
            searchViewModel.uiState.collectLatest {
                when(it) {
                    is SearchUiState.SearchMedicinesSuccess -> {
                        binding.searchLoProgressContainer.visibility = View.GONE
                    }

                    is SearchUiState.Loading -> {
                        binding.searchLoProgressContainer.visibility = View.VISIBLE
                    }

                    else -> null
                }
            }
        }

    }

    override fun onPause() {
        super.onPause()

        Log.d("HomeFragment", "onPause")
    }

    override fun onResume() {
        super.onResume()
        Log.d("HomeFragment", "onResume")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initView(){

    }

}