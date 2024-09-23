package com.blackcows.butakaeyak.ui.search.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.blackcows.butakaeyak.ui.search.MedicineResultFragment
import com.blackcows.butakaeyak.ui.search.SearchHistoryFragment

class SearchViewPager(fragment: Fragment): FragmentStateAdapter(fragment) {
    //TODO: update list.
    private val pages by lazy { listOf(MedicineResultFragment(), SearchHistoryFragment()) }//
    val pageTag = listOf(MedicineResultFragment.TAB_NAME,SearchHistoryFragment.TAB_NAME)//

    override fun getItemCount() = pages.size
    override fun createFragment(position: Int) = pages[position]
}