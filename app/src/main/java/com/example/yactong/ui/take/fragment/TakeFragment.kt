package com.example.yactong.ui.take.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.yactong.R
import com.example.yactong.databinding.FragmentTakeBinding
import com.example.yactong.databinding.FragmentUserBinding

class TakeFragment : Fragment() {

    //binding 설정
    private var _binding: FragmentTakeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTakeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnAdd.setOnClickListener {
                //navigate를 통해서 TakeFragment에서 TakeAddFragment로 이동
                //TakeAddFragment는 layout에 ViewPager2가 있음
               findNavController().navigate(R.id.action_navigation_take_to_navigation_take_add)
            }
        }
    }
}