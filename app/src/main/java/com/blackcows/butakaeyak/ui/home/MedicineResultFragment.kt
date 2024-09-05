package com.blackcows.butakaeyak.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.blackcows.butakaeyak.data.models.Medicine
import com.blackcows.butakaeyak.databinding.FragmentMedicineResultBinding
import com.blackcows.butakaeyak.ui.home.adapter.HomeRecyclerAdapter
import com.blackcows.butakaeyak.ui.home.data.DataSource
private const val TAG = "약 결과"
class MedicineResultFragment : Fragment() {
    //binding 설정
    private var _binding: FragmentMedicineResultBinding? = null
    private val binding get() = _binding!!

    private lateinit var medicineAdapter : HomeRecyclerAdapter

    //viewModel 설정
    private val homeViewModel: HomeViewModel by activityViewModels()

    private var columnCount = 1 //컬럼 갯수 = 1 리니어

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
            medicineAdapter = HomeRecyclerAdapter(object : HomeRecyclerAdapter.ClickListener{
                override fun onItemClick(item: Medicine) {
                    //("디테일 화면 띄움")
                    Log.d(TAG,"${item.id}, ${item.name} ")
                }
                override fun isMedicineChecked(item: Medicine) : Boolean{
                    Log.d(TAG,item.id.toString() + ": "+DataSource.isItemChecked(requireContext(),"MyPillData",item.id.toString()))
                    return DataSource.isItemChecked(requireContext(),"MyPillData",item.id.toString())
                }
                override fun setMedicineChecked(item: Medicine, isChecked: Boolean) {
                    Log.d(TAG,item.id.toString() + ": "+isChecked)
                    DataSource.satItemChecked(requireContext(),"MyPillData",item.id.toString(),isChecked)
                }
//                override fun onMyPillClick(item: Medicine, needAdd: Boolean) {
//                    //("복용중인 약 추가/삭제")
//                    val json = "{\"id:\":\""+item.id.toString()+"\","+
//                                "\"name:\":\""+item.name.toString()+"\","+
//                                "\"enterprise:\":\""+item.enterprise.toString()+"\","+
//                                "\"effect:\":\""+item.effect.toString()+"\","+
//                                "\"instructions:\":\""+item.instructions.toString()+"\","+
//                                "\"warning:\":\""+item.warning.toString()+"\","+
//                                "\"caution:\":\""+item.caution.toString()+"\","+
//                                "\"interaction:\":\""+item.interaction.toString()+"\","+
//                                "\"sideEffect:\":\""+item.sideEffect.toString()+"\","+
//                                "\"storingMethod:\":\""+item.storingMethod.toString()+"\","+
//                                "\"imageUrl:\":\""+item.imageUrl.toString()+"\","+
//                                "\"needAdd:\":\""+needAdd+"\"}"
//                    Log.d("아이템 복용약 누름","${item.id}, ${item.name}, ${needAdd}")
//                    DataSource.saveData(requireContext(),"MyPillData",item.id.toString(),json)
//                }
            })
            resultlist.adapter = medicineAdapter
            resultlist.itemAnimator = null
            homeViewModel.medicineResult.observe(viewLifecycleOwner){
                medicineAdapter.submitList(it.toList())
            }
//            pillAdapter.submitList(dataSource)
        }
    }

    companion object {
        const val ARG_COLUMN_COUNT = "column-count"
        const val TAB_NAME = "결과 화면"

        @JvmStatic
        fun newInstance(columnCount: Int) =
            MedicineResultFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}