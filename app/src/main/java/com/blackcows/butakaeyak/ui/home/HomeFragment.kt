package com.blackcows.butakaeyak.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.blackcows.butakaeyak.databinding.FragmentHomeBinding
import com.blackcows.butakaeyak.firebase.auth.FirebaseAuthManager
import com.google.firebase.FirebaseApp
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPager : ViewPager2

    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val viewpager = binding.homeVp
        homeViewModel.text.observe(viewLifecycleOwner) {
            viewpager.currentItem
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager = binding.homeVp
        binding.homeVp.adapter = HomeViewPager(this@HomeFragment)

        TabLayoutMediator(binding.homeLoTab, binding.homeVp) { tab, position ->
            tab.text = HomeViewPager(this).pageTag[position]
        }.attach()

        binding.homeBtnSearch.setOnClickListener {
            val query = binding.homeEtSearchtext.text.toString()
            homeViewModel.searchPills(query)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}