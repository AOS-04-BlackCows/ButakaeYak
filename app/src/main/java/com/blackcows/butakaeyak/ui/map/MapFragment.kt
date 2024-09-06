package com.blackcows.butakaeyak.ui.map

import android.Manifest
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.blackcows.butakaeyak.BuildConfig
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.databinding.BottomsheetMapDetailBinding
import com.blackcows.butakaeyak.databinding.FragmentMapBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kakao.sdk.common.KakaoSdk
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.camera.CameraAnimation
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles

private const val TAG = "k3f_MapFragment"
class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private val mapViewModel: MapViewModel by activityViewModels()
    private var myPlaceX: Double = 0.0 // 0.0
    private var myPlaceY: Double = 0.0 // 0.0
//    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null // 현재 위치를 가져오기 위한 변수
    private val REQUEST_PERMISSION_LOCATION = 10
//    lateinit var mLastLocation: Location // 위치 값을 가지고 있는 객체
//    private lateinit var mLocationRequest: LocationRequest // 위치 정보
    private lateinit var bottomSheetView: BottomsheetMapDetailBinding
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var kakaoMapCall: KakaoMap
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
//        val mapViewModel = ViewModelProvider(this).get(MapViewModel::class.java)
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        val root: View = binding.root
        checkPermissionForLocation(this)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        KakaoSdk.init(requireContext(), BuildConfig.NATIVE_APP_KEY)
        // 키해쉬 발급 ( 디버그 )
        // val keyHash = Utility.getKeyHash(requireContext())
        // Log.d(TAG, keyHash)
        bottomSheetView = BottomsheetMapDetailBinding.inflate(layoutInflater)
        bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(bottomSheetView.root)
        binding.testBtn1.setOnClickListener {
            bottomSheetDialog.show()
        }
    }
    // 내 위치로 이동
    private fun kakaoMapMoveCamera(kakaoMap: KakaoMap) {
        var cameraUpdate = CameraUpdateFactory.newCenterPosition(LatLng.from(myPlaceY, myPlaceX))
        kakaoMap.moveCamera(cameraUpdate, CameraAnimation.from(500, true, true))
    }


    // 위치 권한이 있는지 확인하는 메서드
    private fun checkPermissionForLocation(fragment: Fragment): Boolean {
        // Android 6.0 Marshmallow 이상에서는 위치 권한에 추가 런타임 권한이 필요
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (fragment.context?.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                gpsInit()
                true
            } else {
                // 권한이 없으므로 권한 요청 알림 보내기
                fragment.requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSION_LOCATION)
                false
            }
        } else {
            true
        }
    }

    // 사용자에게 권한 요청 후 결과에 대한 처리 로직
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                gpsInit()
            } else {
                Log.d(TAG, "onRequestPermissionsResult() _ 권한 허용 거부")
                Toast.makeText(requireContext(), "권한이 없어 해당 기능을 실행할 수 없습니다.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
    // NEW GPS CODE
    fun gpsInit() {
        // 사용자의 위치를 얻을 때는 LocationManager라는 시스템 서비스를 이용
        val manager  = requireContext().getSystemService(LOCATION_SERVICE) as LocationManager
        // 현재 기기에 어떤 위치 제공자가 있는지를 알고 싶다면 LocationManager의 allProviders 프로퍼티를 이용
        var result = "All Providers : "
        val providers = manager.allProviders
        for (provider in providers) {
            result += " $provider. "
        }
        Log.d(TAG, result)  // All Providers : passive, gps, network..
        // 지금 사용할 수 있는 위치 제공자를 알아보려면 getProviders() 함수를 이용
        result = "Enabled Providers : "
        val enabledProviders = manager.getProviders(true)
        for (provider in enabledProviders) {
            result += " $provider. "
        }
        Log.d(TAG, result)  // Enabled Providers : passive, gps, network..
        // 위치정보얻기
        // getAccuracy(): 정확도 || getLatitude(): 위도 || getLongitude(): 경도 || getTime(): 획득 시간
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val location: Location? = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            location?.let{
                myPlaceX = location.longitude // 갱신 된 경도 127.11547410533494
                myPlaceY = location.latitude // 갱신 된 위도 37.40754692649233
                val accuracy = location.accuracy
                val time = location.time
                Log.d(TAG, "$myPlaceX, $myPlaceY, $location, $accuracy, $time")
            }
        }
        kakaoMapInit(myPlaceX, myPlaceY)
        mapViewModel.communicateNetWork(myPlaceX, myPlaceY)
    }
    // 카카오맵
    private fun kakaoMapInit(x: Double, y: Double) {
        // 카카오맵 실행
        Log.d(TAG, "myPlaceX, myPlaceY = $myPlaceX, $myPlaceY")
        KakaoMapUtil(requireContext()).kakaoMapInit(binding.mapView, x, y, mapViewModel) { kakaoMap ->
            kakaoMapCall = kakaoMap
            // 버튼 이벤트 설정
            // 버튼 이벤트를 통해 현재 위치 찾기
            binding.btnLocation.setOnClickListener {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    // 위치 찍는 코드
                    // startLocationUpdates()
                    // 현재 위치로 이동합니다.
                    kakaoMapMoveCamera(kakaoMap)
                }
            }
            // 약국의 데이터가 들어오면 라벨을 찍어준다
            mapViewModel.items.observe(viewLifecycleOwner) { items ->
                Log.d(TAG,"mapViewModel.items Changed")
                kakaoMap.labelManager!!.getLayer()
                kakaoMap.labelManager!!.getLodLayer()
                for (item in items) {
                    val itemResult = with(item){
                        "$placeName||$distance||$placeUrl||$categoryName||$addressName||$roadAddressName||$id||$phone||$categoryGroupCode||$categoryGroupName||$x||$y"
                    }
                    val style = kakaoMap.labelManager?.addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.icon_pharmacy_label)))
                    // 라벨 옵션 지정. 위경도와 스타일 넣기
                    val options = LabelOptions.from(LatLng.from(item.y.toDouble(), item.x.toDouble())).setStyles(style).setTag(itemResult).setClickable(true)
                    // 레이어 가져오기
                    val layer = kakaoMap.labelManager?.layer
                    // 레이어에 라벨 추가
                    layer?.addLabel(options)
                    kakaoMap.setOnLabelClickListener { kakaoMap, labelLayer, label ->
                        Log.d(TAG, label?.tag.toString())
                        val selectTagArr = label?.tag.toString().split("||")
                        bottomSheetView.distance.text = "${selectTagArr[1]}m"
                        bottomSheetView.placeName.text = selectTagArr[0]
                        bottomSheetView.phone.text = selectTagArr[7]
                        bottomSheetView.placeUrl.text = selectTagArr[2]
                        bottomSheetView.addressName.text = selectTagArr[4]
                        bottomSheetView.roadAddressName.text = selectTagArr[5]
                        bottomSheetDialog.show()
                        true
                    }
                    /*  selectTagArr Data
                        [0] placeName = 예시 : 한우리약국,
                        || [1] distance = 예시 : 291,
                        || [2] placeUrl = 예시 : "http://place.map.kakao.com/9578427",
                        || [3] categoryName = 예시 : "의료,건강 > 약국",
                        || [4] addressName = 예시 : "경기 성남시 분당구 야탑동 215",
                        || [5] roadAddressName = 예시 : "경기 성남시 분당구 장미로 139",
                        || [6] id = 예시 : 9578427,
                        || [7] phone = 예시 : "031-708-3399",
                        || [8] categoryGroupCode = 예시 : "PM9",
                        || [9] categoryGroupName = 예시 : "약국",
                        || [10] x = 예시 : 127.13616482305073,
                        || [11] y = 예시 : 37.413583634331886
                    */
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.resume() // MapView 의 resume 호출
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.pause() // MapView 의 pause 호출
    }


}



