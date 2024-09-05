package com.blackcows.butakaeyak.ui.home.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.blackcows.butakaeyak.ui.home.MedicineResultFragment

class HomeViewPager(fragment: Fragment): FragmentStateAdapter(fragment) {
    //TODO: update list.
    private val pages by lazy { listOf(MedicineResultFragment()) }//, FeedFragment()
    val pageTag = listOf(MedicineResultFragment.TAB_NAME)//,FeedFragment.TAB_NAME

    override fun getItemCount() = pages.size
    override fun createFragment(position: Int) = pages[position]
}