package com.blackcows.butakaeyak.ui.schedule.recycler

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.blackcows.butakaeyak.data.models.Friend
import com.blackcows.butakaeyak.ui.schedule.ScheduleDetailFragment
import com.blackcows.butakaeyak.ui.schedule.ScheduleFragment

class FriendViewPager(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    private val fragmentList = mutableListOf<Fragment>()
    private val scheduleProfileList = mutableListOf<ScheduleProfile>()

    override fun getItemCount(): Int = fragmentList.size
    override fun createFragment(position: Int): Fragment = fragmentList[position]
    //TODO: notifyDataSetChanged 제대로 작동하기 위해서 필요하다는데 long id를 무엇으로 설정할건가?
    //  https://stackoverflow.com/questions/68382901/what-is-purpose-of-overriding-containsitem-and-getitemid-in-fragmentstatead

//    override fun containsItem(itemId: Long): Boolean {
//        return fragmentList.any { it.value.fragId == itemId }
//    }

    fun setScheduleProfiles(profiles: List<ScheduleProfile>) {
        profiles.forEachIndexed { i, it ->
            if(scheduleProfileList.contains(it)) {
                removeFriend(it)
            }
            addFriend(it, i==0)
        }

        notifyDataSetChanged()
    }

    fun getFragment(viewPager2: ViewPager2, scheduleProfile: ScheduleProfile) {
        val index = scheduleProfileList.indexOf(scheduleProfile)

        if(index == -1) {
            throw Exception("FriendViewPager: 등록되지 않은 scheduleProfile 입니다.")
        }

        viewPager2.currentItem = index
    }
    fun getFragment(viewPager2: ViewPager2, scheduleProfileId: String) {
        val index = scheduleProfileList.indexOfFirst {
            it.userId == scheduleProfileId
        }
        if(index == -1) {
            throw Exception("FriendViewPager: 등록되지 않은 scheduleProfile 입니다.")
        }
        viewPager2.currentItem = index
    }

    private fun addFriend(scheduleProfile: ScheduleProfile, isMine: Boolean) {
        val scheduleFragment = ScheduleDetailFragment.newInstance(scheduleProfile.userId, isMine)
        fragmentList.add(scheduleFragment)
        scheduleProfileList.add(scheduleProfile)
    }

    private fun removeFriend(scheduleProfile: ScheduleProfile) {
        if(scheduleProfileList.contains(scheduleProfile)) {
            val index = scheduleProfileList.indexOf(scheduleProfile)
            scheduleProfileList.removeAt(index)
            fragmentList.removeAt(index)
        } else {
            throw Exception("FriendViewPager: 등록되지 않은 scheduleProfile 입니다.")
        }
    }


}