package com.blackcows.butakaeyak.ui.take.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.databinding.FragmentTermsBinding
import com.blackcows.butakaeyak.ui.navigation.MainNavigation

// TODO 이용 약관 Fragment 작성

class TermsFragment : Fragment() {

    //binding 설정
    private var _binding: FragmentTermsBinding? = null
    private val binding get() = _binding!!

    //뒤로가기 설정
    private val onBackPressed = {
            parentFragmentManager.beginTransaction().setCustomAnimations(R.anim.move_enter,R.anim.move_exit).remove(
            this
        ).commit()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTermsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        })

        binding.ivBack.setOnClickListener {
            MainNavigation.popCurrentFragment()
        }

        binding.clTerms1.setOnClickListener {
            if (binding.tvTerms1.visibility == View.GONE) {
                binding.tvTerms1.visibility = View.VISIBLE
                binding.ivTerms1.animate().apply {
                    duration = 300
                    rotation(90f)
                }
            } else {
                binding.tvTerms1.visibility = View.GONE
                binding.ivTerms1.animate().apply {
                    duration = 300
                    rotation(0f)
                }
            }
        }

        binding.clTerms2.setOnClickListener {
            if (binding.tvTerms2.visibility == View.GONE) {
                binding.tvTerms2.visibility = View.VISIBLE
                binding.ivTerms2.animate().apply {
                    duration = 300
                    rotation(90f)
                }
            } else {
                binding.tvTerms2.visibility = View.GONE
                binding.ivTerms2.animate().apply {
                    duration = 300
                    rotation(0f)
                }
            }
        }

        binding.clTerms3.setOnClickListener {
            if (binding.tvTerms3.visibility == View.GONE) {
                binding.tvTerms3.visibility = View.VISIBLE
                binding.ivTerms3.animate().apply {
                    duration = 300
                    rotation(90f)
                }
            } else {
                binding.tvTerms3.visibility = View.GONE
                binding.ivTerms3.animate().apply {
                    duration = 300
                    rotation(0f)
                }
            }
        }

        binding.clTerms4.setOnClickListener {
            if (binding.tvTerms4.visibility == View.GONE) {
                binding.tvTerms4.visibility = View.VISIBLE
                binding.ivTerms4.animate().apply {
                    duration = 300
                    rotation(90f)
                }
            } else {
                binding.tvTerms4.visibility = View.GONE
                binding.ivTerms4.animate().apply {
                    duration = 300
                    rotation(0f)
                }
            }
        }

        binding.clTerms5.setOnClickListener {
            if (binding.tvTerms5.visibility == View.GONE) {
                binding.tvTerms5.visibility = View.VISIBLE
                binding.ivTerms5.animate().apply {
                    duration = 300
                    rotation(90f)
                }
            } else {
                binding.tvTerms5.visibility = View.GONE
                binding.ivTerms5.animate().apply {
                    duration = 300
                    rotation(0f)
                }
            }
        }

        binding.clTerms6.setOnClickListener {
            if (binding.tvTerms6.visibility == View.GONE) {
                binding.tvTerms6.visibility = View.VISIBLE
                binding.ivTerms6.animate().apply {
                    duration = 300
                    rotation(90f)
                }
            } else {
                binding.tvTerms6.visibility = View.GONE
                binding.ivTerms6.animate().apply {
                    duration = 300
                    rotation(0f)
                }
            }
        }

        binding.clTerms7.setOnClickListener {
            if (binding.tvTerms7.visibility == View.GONE) {
                binding.tvTerms7.visibility = View.VISIBLE
                binding.ivTerms7.animate().apply {
                    duration = 300
                    rotation(90f)
                }
            } else {
                binding.tvTerms7.visibility = View.GONE
                binding.ivTerms7.animate().apply {
                    duration = 300
                    rotation(0f)
                }
            }
        }

        binding.clTerms8.setOnClickListener {
            if (binding.tvTerms8.visibility == View.GONE) {
                binding.tvTerms8.visibility = View.VISIBLE
                binding.ivTerms8.animate().apply {
                    duration = 300
                    rotation(90f)
                }
            } else {
                binding.tvTerms8.visibility = View.GONE
                binding.ivTerms8.animate().apply {
                    duration = 300
                    rotation(0f)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}