package com.blackcows.butakaeyak.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.blackcows.butakaeyak.MainViewModel
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.models.HomeRvGroup
import com.blackcows.butakaeyak.data.models.MedicineDetail
import com.blackcows.butakaeyak.data.models.MedicineGroup
import com.blackcows.butakaeyak.databinding.FragmentHomeBinding
import com.blackcows.butakaeyak.ui.home.adapter.HomeTodayMedicineRvAdapter
import com.blackcows.butakaeyak.ui.home.adapter.HomeViewPagerAdapter
import com.blackcows.butakaeyak.ui.map.MapFragment
import com.blackcows.butakaeyak.ui.navigation.FragmentTag
import com.blackcows.butakaeyak.ui.navigation.MainNavigation
import com.blackcows.butakaeyak.ui.take.fragment.TakeAddFragment
import com.blackcows.butakaeyak.ui.textrecognition.OCRActivity
import com.blackcows.butakaeyak.ui.textrecognition.OcrFragment
import com.blackcows.butakaeyak.ui.viewmodels.UserViewModel
import io.ktor.util.date.WeekDay
import java.time.LocalDate


private const val TAG = "HomeFragment"
class HomeFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() = HomeFragment()
    }

    lateinit var fab_open: Animation
    lateinit var fab_close: Animation
    var openFlag = false

    private val homeViewModel: HomeViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()
    //binding 설정
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var todayMedicineGroupRvAdapter : HomeTodayMedicineRvAdapter
//    private val item : MyMedicine? = null

    //ViewPager 설정
    private val homeViewPagerAdapter by lazy {
        HomeViewPagerAdapter {
            // mainViewModel.cancelFavoritePharmacy(it.name)
            // TODO mainViewModel에 약 예약으로 가기 추가하고 적용
            // TODO onClickItem 옵션추가
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        mainViewModel.pharmacies.observe(viewLifecycleOwner) {
            Log.d("UserFragment", "pharmacies size: ${it.size}")
        }
        // MockUp Data
        /*
        뷰페이저 제거
        val mockUpMedicineGroup = listOf(
            MedicineGroup("0001","그룹 1","1",listOf<MedicineDetail>(MedicineDetail("","","","","","","",""),MedicineDetail("","","","","","","","")),listOf("커스텀 약","커스텀 약"),listOf(), LocalDate.now(), LocalDate.now(),listOf(WeekDay.SUNDAY,WeekDay.MONDAY),listOf("07:50", "12:50"),listOf<String>()),
            MedicineGroup("0002","그룹 2","2",listOf<MedicineDetail>(MedicineDetail("","","","","","","",""),MedicineDetail("","","","","","","","")),listOf(),listOf(), LocalDate.now(), LocalDate.now(),listOf(WeekDay.SUNDAY,WeekDay.MONDAY),listOf("01:10", "04:10"),listOf<String>()),
            MedicineGroup("0003","그룹 3","3",listOf<MedicineDetail>(MedicineDetail("","","","","","","",""),MedicineDetail("","","","","","","","")),listOf("커스텀 약1","커스텀 약2"),listOf(), LocalDate.now(), LocalDate.now(),listOf(WeekDay.SUNDAY,WeekDay.MONDAY),listOf("11:59", "14:59"),listOf<String>())
        )
        homeViewPagerAdapter.submitList(mockUpMedicineGroup)
        binding.vpTodayMedicine.adapter = homeViewPagerAdapter
        val dotsIndicator = binding.dotsIndicator
        dotsIndicator.attachTo(binding.vpTodayMedicine)
        */

        //애니메이션 변수
        fab_open = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_open)
        fab_close = AnimationUtils.loadAnimation(requireContext(),R.anim.fab_close)

        //초기 애니메이션은 닫혀있는 애니메이션
        binding.btnAddMedicineContainer.startAnimation(fab_close)

        binding.btnAddMedicineGroup.setOnClickListener(this)
        binding.btnAddMedicineContainer.setOnClickListener(this)

        binding.btnAddMedicineContainer.isClickable = false
        fragmentInit()
        //
//        fragList = arrayListOf(FragmentControl(), FragmentState(), FragmentPolicy())
//
//        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()

        /* 고려사항 정리
        1. 아이템의 출력은 alarm의 갯수만큼
        2. 아이템의 정렬 순서도 alarm으로

        TODO MedicineGroupRepositoryImpl 에 getMedicineGroup 이 어떤식으로 데이터를 불러오는지 파악해야 submitList가 가능함. 체크필요
        */

        todayMedicineGroupRvAdapter = HomeTodayMedicineRvAdapter (object : HomeTodayMedicineRvAdapter.ClickListener {
            override fun onFavoriteClick(item: MedicineGroup, position: Int) {
//                TODO("Not yet implemented")
            }

            override fun onCallClick(item: MedicineGroup) {
//                TODO("Not yet implemented")
            }
        })
        binding.homeRvTodayMedicineGroup.run {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = todayMedicineGroupRvAdapter
            itemAnimator = null
        }
        // 아직 데이터가 안들어옴
        homeViewModel.getTodayMedicine(userViewModel.user.value?.id?: "guest")
        homeViewModel.medicineGroup.observe(viewLifecycleOwner) {
            Log.d(TAG,"homeViewModel.medicineGroup.value = ${homeViewModel.medicineGroup.value}")
            homeViewModel.medicineGroup.value
//            todayMedicineGroupRvAdapter.submitList()
        }
        val mockUpMedicineGroup = listOf(
            MedicineGroup("0001","그룹 1","1",listOf<MedicineDetail>(MedicineDetail("","","","","","","",""),MedicineDetail("","","","","","","","")),listOf("커스텀 약","커스텀 약"),listOf(), LocalDate.now(), LocalDate.now(),listOf(WeekDay.SUNDAY,WeekDay.MONDAY),listOf("07:50", "12:50"),listOf<String>()),
            MedicineGroup("0002","그룹 2","2",listOf<MedicineDetail>(MedicineDetail("","","","","","","",""),MedicineDetail("","","","","","","","")),listOf(),listOf(), LocalDate.now(), LocalDate.now(),listOf(WeekDay.SUNDAY,WeekDay.MONDAY),listOf("01:10", "04:10"),listOf<String>()),
            MedicineGroup("0003","그룹 3","3",listOf<MedicineDetail>(MedicineDetail("","","","","","","",""),MedicineDetail("","","","","","","","")),listOf("커스텀 약1","커스텀 약2"),listOf(), LocalDate.now(), LocalDate.now(),listOf(WeekDay.SUNDAY,WeekDay.MONDAY),listOf("11:59", "14:59"),listOf<String>())
        )
        // 고려사항 정리
        // 이럴떄 groupId && alarmTime 이 같은게 있다면 true
        // tokenTime 체커
//        val alarms: List<String> format: "10:30", "12:40"
//        val hasTaken: List<String> format: "2024-09-12 10:30", "2024-09-12 10:30"
        fun isHasTakenTime (id: String, alarm: String): Boolean {
            val sameGroupTaken = mockUpMedicineGroup.first{ it.id == id }.hasTaken
            for (i in sameGroupTaken) {
                if (i.split(" ")[1] == alarm) {
                    return true
                }
            }
            return false
        }
        var medicineGroupList: MutableList<HomeRvGroup> = mutableListOf()
        fun medicineGroupConverter (list: List<MedicineGroup>): List<HomeRvGroup> {
            medicineGroupList = mutableListOf()
            for (i in list) {
                for (j in i.alarms) {
                    Log.d("$TAG HomeRvGroup", "HomeRvGroup(${i.id},${j},${i.name},${isHasTakenTime(i.id, j)})")
                    medicineGroupList += HomeRvGroup(i.id, j, i.name, isHasTakenTime(i.id, j))
                }
            }
            medicineGroupList.sortBy { it.alarmTime }
            return medicineGroupList
        }
        binding.homeAlarmViewMore.setOnClickListener {
            Log.d("$TAG 1", "medicineGroupConverter(mockUpMedicineGroup) = $mockUpMedicineGroup")
            Log.d("$TAG 2", "medicineGroupConverter(mockUpMedicineGroup) = ${medicineGroupConverter(mockUpMedicineGroup)}")
            medicineGroupConverter(mockUpMedicineGroup)
            todayMedicineGroupRvAdapter.submitList(medicineGroupList)
        }
        Log.d(TAG,"mainViewModel.getMyMedicineList() = ${mainViewModel.myMedicines.value}")

        return root
    }

    //클릭된 버튼에 의해 프래그먼트를 변경
    override fun onClick(v: View){
        when(v.id){
            R.id.btn_add_medicine_group -> anim()
            R.id.btn_add_medicine_container ->{
                anim()
            }
        }
    }

    fun anim () {
        if (openFlag){
            // Fab이 눌려있을때 닫는 애니메이션
            binding.btnAddMedicineContainer.startAnimation(fab_close)
            binding.btnAddMedicineContainer.isClickable = false
            openFlag = false
        } else {
            // Fab이 눌리지 않았을 때 여는 애니메이션
            binding.btnAddMedicineContainer.startAnimation(fab_open)
            binding.btnAddMedicineContainer.isClickable = true
            openFlag = true
        }
    }
    /* TODO
        응애 플로팅버튼 만들어라 응애
        뷰모델 구성해보기
        책상정리하기
    */

    private fun fragmentInit () {
        // 나랑 가까운 약국
        binding.homeConnection1.setOnClickListener{
            MainNavigation.addFragment(MapFragment(), FragmentTag.MapFragment)
        }
        // 이게 무슨 약이지?
        binding.homeConnection2.setOnClickListener{
            Toast.makeText(context, "homeConnection2 is not ready", Toast.LENGTH_SHORT).show()
        }
        // 직접 등록
        binding.btnAddMedicine1.setOnClickListener{
            MainNavigation.addFragment(TakeAddFragment(), FragmentTag.TakeAddFragment)
            anim()
        }
        // 사진 등록
        binding.btnAddMedicine2.setOnClickListener{
//            startActivity(Intent(requireActivity(), OCRActivity::class.java))
            MainNavigation.addFragment(OcrFragment(), FragmentTag.OCRFragment)
            anim()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
    }
}