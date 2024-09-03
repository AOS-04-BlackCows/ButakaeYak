package com.blackcows.butakaeyak.ui.take

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.blackcows.butakaeyak.ui.take.fragment.CycleFragment
import com.blackcows.butakaeyak.ui.take.fragment.FormFragment
import com.blackcows.butakaeyak.ui.take.fragment.NameFragment
import com.blackcows.butakaeyak.ui.take.fragment.TimeFragment

class TakeViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity){
    private lateinit var takeViewPagerAdapter: TakeViewPagerAdapter
    val fragments = listOf<Fragment>(NameFragment(), FormFragment(), TimeFragment(), CycleFragment())

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}