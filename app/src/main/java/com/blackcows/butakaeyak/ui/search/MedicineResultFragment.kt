package com.blackcows.butakaeyak.ui.search

import android.os.Bundle
import android.util.ArraySet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blackcows.butakaeyak.MainViewModel
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.databinding.BottomsheetSearchDetailBinding
import com.blackcows.butakaeyak.databinding.FragmentMedicineResultBinding
import com.blackcows.butakaeyak.ui.navigation.FragmentTag
import com.blackcows.butakaeyak.ui.navigation.MainNavigation
import com.blackcows.butakaeyak.ui.search.adapter.SearchRecyclerAdapter
import com.blackcows.butakaeyak.ui.take.fragment.TakeAddFragment
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog

private const val TAG = "약 결과"
class MedicineResultFragment : Fragment() {
    //binding 설정
    private var _binding: FragmentMedicineResultBinding? = null
    private val binding get() = _binding!!

    private lateinit var medicineAdapter : SearchRecyclerAdapter

    //viewModel 설정
    private val searchViewModel: SearchViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    private var columnCount = 1 //컬럼 갯수 = 1 리니어
    private var touch_logs = mutableListOf<Medicine>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMedicineResultBinding.inflate(inflater, container, false)
        val root = binding.root

        // Set the adapter
        if (root is RecyclerView) {
            with(root) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
            }
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //더미 데이터
//        val dataSource = DataSource.getDataSoures().getMedicineList()

        binding.apply {
            medicineAdapter = SearchRecyclerAdapter(object : SearchRecyclerAdapter.ClickListener{
                override fun onItemClick(item: Medicine) {
                    touch_logs.add(item)
                    val bottomSheetView = BottomsheetSearchDetailBinding.inflate(layoutInflater)
                    val bottomSheetDialog = BottomSheetDialog(requireContext())
                    with(bottomSheetView){
                        Glide.with(root).load(item.imageUrl?: R.drawable.medicine).into(detailIvMedicine)
                        detailTvName.text = item.name
                        detailTvEnterprise.text = item.enterprise
                        detailTvEffect.text = item.effect
                        detailTvInstructions.text = item.instructions
                        detailTvWarning.text = item.warning
                        detailTvCaution.text = item.caution
                        detailTvInteraction.text = item.interaction
                        detailTvSideEffect.text = item.sideEffect
                        detailTvStoringMethod.text = item.storingMethod
                    }
                    bottomSheetDialog.setContentView(bottomSheetView.root)
                    bottomSheetDialog.show()

//                    MainNavigation.addFragment(
//                        SearchDetailFragment.newInstance(item),
//                        FragmentTag.SearchDetailFragmentInSearch
//                    )
                    Log.d(TAG,"${item.id}, ${item.name} ")
                }
                override fun isMedicineChecked(item: Medicine) : Boolean {
                    val result = mainViewModel.isMyMedicine(item.id!!)
                    Log.d(TAG,"${item.id}: $result")
                    return result
                }
                override fun setMedicineChecked(item: Medicine, isChecked: Boolean) {
                    val hasIt = mainViewModel.isMyMedicine(item.id!!)
                    Log.d(TAG,"Do I have item:${item.id}? :$hasIt")
                    if(hasIt) {
                        Toast.makeText(requireContext(), "이미 복용 중인 약입니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        MainNavigation.addFragment(
                            TakeAddFragment.newInstance(item), FragmentTag.TakeAddFragment
                        )
                    }
                }
            })
            resultlist.adapter = medicineAdapter
            resultlist.itemAnimator = null
            searchViewModel.medicineResult.observe(viewLifecycleOwner){
                if(it.isNotEmpty()) Log.d(TAG, "Class: ${it[0]::class.simpleName}")
                medicineAdapter.submitList(it)
            }
        }
    }



    companion object {
        const val ARG_COLUMN_COUNT = "column-count"
        const val TAB_NAME = "결과 화면"
        const val TOUCH_LOGS = "touch_logs"

        @JvmStatic
        fun newInstance(columnCount: Int) =
            MedicineResultFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}