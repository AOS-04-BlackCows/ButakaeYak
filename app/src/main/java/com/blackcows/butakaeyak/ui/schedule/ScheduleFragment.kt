package com.blackcows.butakaeyak.ui.schedule

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.models.Friend
import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.databinding.FragmentScheduleBinding
import com.blackcows.butakaeyak.ui.navigation.FragmentTag
import com.blackcows.butakaeyak.ui.note.recycler.NoteRvDecoration
import com.blackcows.butakaeyak.ui.schedule.recycler.FriendViewPager
import com.blackcows.butakaeyak.ui.schedule.recycler.ProfileRvAdapter
import com.blackcows.butakaeyak.ui.schedule.recycler.ProfileRvDecoration
import com.blackcows.butakaeyak.ui.schedule.recycler.ScheduleProfile
import com.blackcows.butakaeyak.ui.schedule.recycler.ScheduleRvAdapter
import com.blackcows.butakaeyak.ui.take.fragment.CycleFragment
import com.blackcows.butakaeyak.ui.viewmodels.UserViewModel

class ScheduleFragment : Fragment() {
    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!

    private val scheduleViewModel: ScheduleViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()

    private val myId by lazy {
        userViewModel.user.value!!.id
    }
    private var curScheduleProfileId = ""

    private val profileRvAdapter = ProfileRvAdapter() { userId ->
        if(curScheduleProfileId == userId) {
            return@ProfileRvAdapter
        }

        curScheduleProfileId = userId

        viewPagerAdapter.getFragment(binding.scheduleDetailViewPager, curScheduleProfileId)
    }

    private lateinit var viewPagerAdapter: FriendViewPager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScheduleBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObserver()

        if(userViewModel.user.value == null) {
            binding.loginGuideCl.visibility = View.VISIBLE
            return
        } else {
            binding.loginGuideCl.visibility = View.GONE
        }

        curScheduleProfileId = myId

        with(binding) {
            profileRv.run {
                adapter = profileRvAdapter
                addItemDecoration(ProfileRvDecoration.getLinearDecoSimpleItem())
            }

            profileAddBtn.setOnClickListener {
                //TODO: open the friend dialog.
                //  and if scheduleProfile is changed, call the method: scheduleViewModel.getFriends(userId)
            }
        }


        initFriendsViewPager()
    }

    private fun initFriendsViewPager() {
        viewPagerAdapter = FriendViewPager(requireActivity())
        binding.scheduleDetailViewPager.adapter = viewPagerAdapter

        scheduleViewModel.getFriendProfile(userViewModel.user.value!!.id)
    }

    private fun setObserver() {

        userViewModel.user.observe(viewLifecycleOwner) { user ->
            Log.d("UserViewModel", "ScheduleFragment: user is null? :${user==null}")
            if(user == null) {
                binding.loginGuideCl.visibility = View.VISIBLE
            } else {
                binding.loginGuideCl.visibility = View.GONE
            }
        }

        scheduleViewModel.scheduleProfile.observe(viewLifecycleOwner) { friendsProfiles ->
            if(userViewModel.user.value == null) return@observe

            val myScheduleProfile = with(userViewModel.user.value!!) {
                ScheduleProfile(id, name, profileUrl!!)
            }

            val list = mutableListOf(myScheduleProfile).apply {
                addAll(friendsProfiles)
            }

            profileRvAdapter.submitList(list)

            Log.d("ScheduleFragment", "size: ${profileRvAdapter.currentList.size}")

            viewPagerAdapter.setScheduleProfiles(list)
            viewPagerAdapter.getFragment(binding.scheduleDetailViewPager, myScheduleProfile)
        }
    }
}