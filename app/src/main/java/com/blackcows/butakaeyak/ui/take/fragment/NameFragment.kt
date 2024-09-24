package com.blackcows.butakaeyak.ui.take.fragment

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.models.TakeAddMedicine
import com.blackcows.butakaeyak.databinding.FragmentNameBinding
import com.blackcows.butakaeyak.ui.DrawableNameToResource
import com.blackcows.butakaeyak.ui.navigation.MainNavigation
import com.blackcows.butakaeyak.ui.take.FormSelectDialog
import com.blackcows.butakaeyak.ui.take.TakeAddViewModel
import com.blackcows.butakaeyak.ui.take.adapter.NameAdapter
import com.blackcows.butakaeyak.ui.take.data.NameItem

class NameFragment : Fragment() {

    //binding 설정
    private var _binding: FragmentNameBinding? = null
    private val binding get() = _binding!!
    private val takeAddViewModel : TakeAddViewModel by activityViewModels()
    private lateinit var adapter : NameAdapter

    //data class
    private val mItems = mutableListOf<NameItem>()

    //TODO: 여기!
    private val onBackPressed = {
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.move_enter, R.anim.move_exit)
            .remove(this)
            .commit()

        MainNavigation.popCurrentFragment()
    }

    //TODO: 여기!
    //bundle에서 medicine 가져오기
//    private val medicine: Medicine by lazy {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            arguments?.getParcelable(MEDICINE_DATA, Medicine::class.java)!!
//        } else {
//            @Suppress("DEPRECATION")
//            arguments?.getParcelable(MEDICINE_DATA)!!
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNameBinding.inflate(inflater, container, false)
        val root: View = binding.root

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.d("NameFragment", "Back Pressed!")
                onBackPressed()
            }
        })

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnMedicineForm.setOnClickListener {
                val formDialog = FormSelectDialog(requireContext(),object :
                    FormSelectDialog.OnFormSelectListener {
                    override fun onFormSelected(image: Drawable) {
                        btnMedicineForm.background = image
                    }
                }, btnMedicineForm.background)
                formDialog.show()
            }

//            etMedicineName.setText(medicine.name!!)

//            val nameList

            takeAddViewModel.loadNames()
            adapter = NameAdapter(object : NameAdapter.ClickListener {
                override fun onMinusClick(item: TakeAddMedicine) {
                    // TODO("Not yet implemented")
                }

                override fun onSearchClick(item: TakeAddMedicine) {
                    // TODO("Not yet implemented")
                }
            }
            )
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            takeAddViewModel.nameRvGroup.observe(viewLifecycleOwner) {
                adapter.submitList(it)
                if(it.isNotEmpty()){
                    btnNext.apply{
                        isEnabled = true
                        setBackgroundResource(R.color.green)
                        setTextColor(Color.WHITE)
                    }
                } else {
                    btnNext.apply{
                        isEnabled = false
                        setBackgroundResource(R.color.gray)
                        setTextColor(Color.DKGRAY)
                    }
                }
            }
            Log.d("제발되어라", "btnMedicineForm.background.toString() = ${btnMedicineForm.background.toString()}")
            btnMedicineForm.setBackgroundResource(R.drawable.medicine_type_14)
            btnPlusMinus.setOnClickListener {
                takeAddViewModel.addNames("medicine_type_1", etMedicineName.text.toString())
                etMedicineName.text.clear()
            }

            tvSize.text = "총 ${adapter.itemCount}개의 약이 등록 예정"


                btnNext.setOnClickListener {
                    if(etMedicineName.length() > 0){
                        Log.d("버튼","버튼 눌림")
                        parentFragmentManager.beginTransaction()
                            .add(
                                R.id.fragment_container, CycleFragment()
                            )
                            .addToBackStack(null)
                            .commit()
                        }
                    }


        }

        //TODO: 여기!
        binding.ivBack.setOnClickListener {
            Log.d("NameFragment", "Back Pressed2")
            onBackPressed()
        }

        binding.etMedicineName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                binding.apply{
//                    if(etMedicineName.length() > 0){
//                        btnNext.apply{
//                            isEnabled = true
//                            setBackgroundResource(R.color.green)
//                            setTextColor(Color.WHITE)
//                        }
//                    }
//                    else{
//                        btnNext.apply{
//                            isEnabled = false
//                            setBackgroundResource(R.color.gray)
//                            setTextColor(Color.DKGRAY)
//                        }
//                    }
//                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        //editText 클릭 시 키보드 올리는 코드
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        binding.apply{
                inputMethodManager.showSoftInput(etMedicineName, 0)
        }
    }

    private fun hideKeyboard(){
        if(activity != null && requireActivity().currentFocus != null){
            val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    companion object {
//        private const val MEDICINE_DATA = "medicine_data"
//
//        @JvmStatic
//        fun newInstance(medicine: Medicine) =
//            NameFragment().apply {
//                arguments = Bundle().apply {
//                    putParcelable(MEDICINE_DATA, medicine)
//                }
//            }
//    }
}