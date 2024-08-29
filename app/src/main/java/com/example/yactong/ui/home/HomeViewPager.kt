package com.example.yactong.ui.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class HomeViewPager(fragment: Fragment): FragmentStateAdapter(fragment) {
    //TODO: update list.
    private val pages = listOf<Fragment>(ResultsFragment())
    val pageTag = listOf(ResultsFragment.TAB_NAME)

    override fun getItemCount() = pages.size
    override fun createFragment(position: Int) = pages[position]
}