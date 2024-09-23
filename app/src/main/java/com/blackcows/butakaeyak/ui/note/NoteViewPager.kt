package com.blackcows.butakaeyak.ui.note

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class NoteViewPager(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    private val fragments = listOf(NoteGroupFragment(), NoteDateFragment())

    val tabName = listOf("그룹", "날짜")

    override fun getItemCount(): Int = fragments.size
    override fun createFragment(position: Int): Fragment = fragments[position]
}