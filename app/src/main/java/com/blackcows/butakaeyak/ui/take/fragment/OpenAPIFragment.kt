package com.blackcows.butakaeyak.ui.take.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.databinding.FragmentOpenApiBinding
import com.blackcows.butakaeyak.ui.navigation.MainNavigation

class OpenAPIFragment : Fragment() {

    private var _binding: FragmentOpenApiBinding? = null
    private val binding get() = _binding!!

    //뒤로가기 설정
    private val onBackPressed = {
        parentFragmentManager.beginTransaction().setCustomAnimations(R.anim.move_enter, R.anim.move_exit).remove(
            this
        ).commit()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentOpenApiBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onBackPressed()
                }
            })

        binding.ivBack.setOnClickListener {
            MainNavigation.popCurrentFragment()
        }
    }
}