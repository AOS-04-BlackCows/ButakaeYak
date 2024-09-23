package com.blackcows.butakaeyak.ui.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.databinding.FragmentNoteBinding
import com.google.android.material.tabs.TabLayoutMediator

class NoteFragment : Fragment() {

    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!

    private val noteViewModel: NoteViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        val tabLayout = binding.tabLo
        val noteAdapter = NoteViewPager(requireActivity())
        val viewPager = binding.viewpager.apply {
            adapter = noteAdapter
        }

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = noteAdapter.tabName[position]
        }.attach()
    }
}