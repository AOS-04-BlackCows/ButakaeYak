package com.blackcows.butakaeyak.ui.map

import android.Manifest
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.blackcows.butakaeyak.BuildConfig
import com.blackcows.butakaeyak.MainViewModel
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.data.models.KakaoPlacePharmacy
import com.blackcows.butakaeyak.data.source.LocalDataSource
import com.blackcows.butakaeyak.databinding.BottomsheetMapDetailBinding
import com.blackcows.butakaeyak.databinding.BottomsheetMapListBinding
import com.blackcows.butakaeyak.databinding.FragmentMapBinding
import com.blackcows.butakaeyak.ui.map.adapter.PharmacyListRvAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.CompetitionType
import com.kakao.vectormap.label.CompetitionUnit
import com.kakao.vectormap.label.LabelLayer
import com.kakao.vectormap.label.LabelLayerOptions
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.label.OrderingType


private const val TAG = "k3f_MapFragment"
class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private val mapViewModel: MapViewModel by activityViewModels()
    private var myPlaceLongtudeX: Double = 127.11547410533494 // 임시 127.11547410533494
    private var myPlaceLatitudeY: Double = 37.40754692649233 // 임시 37.40754692649233
    private var viewPlaceLongtudeX: Double = myPlaceLongtudeX // 임시 127.11547410533494
    private var viewPlaceLatitudeY: Double = myPlaceLatitudeY // 임시 37.40754692649233
    private var locationStatus = false
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
//    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null // 현재 위치를 가져오기 위한 변수
//    lateinit var mLastLocation: Location // 위치 값을 가지고 있는 객체
//    private lateinit var mLocationRequest: LocationRequest // 위치 정보
    private lateinit var bottomSheetDetailView: BottomsheetMapDetailBinding
    private lateinit var bottomSheetDetailDialog: BottomSheetDialog
    private lateinit var bottomSheetListView: BottomsheetMapListBinding
    private lateinit var bottomSheetListDialog: BottomSheetDialog
    private lateinit var kakaoMapCall: KakaoMap
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var pharmacyListRvAdapter : PharmacyListRvAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        KakaoSdk.init(requireContext(), BuildConfig.NATIVE_APP_KEY)
        // 키해쉬 발급 ( 디버그 )
         val keyHash = Utility.getKeyHash(requireContext())
         Log.d(TAG, "keyHash : $keyHash")



        bottomSheetDetailView = BottomsheetMapDetailBinding.inflate(layoutInflater)
        bottomSheetDetailDialog = BottomSheetDialog(requireContext())
        bottomSheetDetailDialog.setContentView(bottomSheetDetailView.root)

        bottomSheetListView = BottomsheetMapListBinding.inflate(layoutInflater)
        bottomSheetListDialog = BottomSheetDialog(requireContext())
        bottomSheetListDialog.setContentView(bottomSheetListView.root)

        kakaoMapInit()

        //ViewPager 설정
        pharmacyListRvAdapter = PharmacyListRvAdapter (object : PharmacyListRvAdapter.ClickListener {
            override fun onViewPharmacyDetail(item: KakaoPlacePharmacy) {
                bottomSheetListDialog.cancel()
                with(bottomSheetDetailView) {
                    distance.text = "${item.distance}m"
                    placeName.text = item.placeName
                    phone.text = item.phone
                    addressName.text = item.addressName
                    roadAddressName.text = item.roadAddressName
                    if (mainViewModel.isPharmacyChecked(item.id)) {
                        btnFavorite.setImageResource(R.drawable.icon_favorite_active)
                    } else {
                        btnFavorite.setImageResource(R.drawable.icon_favorite)
                    }
                    btnFavorite.setOnClickListener {
                        if (mainViewModel.isPharmacyChecked(item.id)) {
                            mainViewModel.cancelFavoritePharmacy(item.id)
                            btnFavorite.setImageResource(R.drawable.icon_favorite)
                        } else {
                            mainViewModel.addToFavoritePharmacyList(item)
                            btnFavorite.setImageResource(R.drawable.icon_favorite_active)
                        }
                    }
                    detailBtnCall.setOnClickListener {
                        val phone = item.phone
                        if (phone.isNotEmpty()) {
                            val intent = Intent(Intent.ACTION_DIAL)
                            intent.data = Uri.parse("tel:$phone")
                            startActivity(intent)
                        } else {
                            Toast.makeText(context, "유효한 전화번호가 아닙니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    detailBtnFindRoad.setOnClickListener {
                        val url = item.placeUrl
                        if (url.isNotEmpty()) {
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data = Uri.parse(url)
                            startActivity(intent)
                        } else {
                            Toast.makeText(context, "유효한 URL이 아닙니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                bottomSheetDetailDialog.show()
            }
            // 즐겨찾기 아이콘 변경은 리싸이클러뷰에 작성되어있음.
            override fun onFavoriteClick(item: KakaoPlacePharmacy, position: Int) {
                if (mainViewModel.isPharmacyChecked(item.id)) {
                    mainViewModel.cancelFavoritePharmacy(item.id)
                } else {
                    mainViewModel.addToFavoritePharmacyList(item)
                }
            }
            override fun onCallClick(item: KakaoPlacePharmacy) {
                val phone = item.phone
                if (phone.isNotEmpty()) {
                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = Uri.parse("tel:$phone")
                    startActivity(intent)
                } else {
                    Toast.makeText(context, "유효한 전화번호가 아닙니다.", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFindRoadClick(item: KakaoPlacePharmacy) {
                val url = item.placeUrl
                if (url.isNotEmpty()) {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                } else {
                    Toast.makeText(context, "유효한 URL이 아닙니다.", Toast.LENGTH_SHORT).show()
                }
            }
        })
        bottomSheetListView.bottomsheetMapList.run {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = pharmacyListRvAdapter
            itemAnimator = null
        }
        binding.btnPharmacyList.setOnClickListener {
            Log.d(TAG, "mapViewModel.items = ${mapViewModel.items.value}")
            bottomSheetListView.mapListTitle.text = "약국 목록"
            pharmacyListRvAdapter.submitList(mapViewModel.items.value)
            bottomSheetListDialog.show()
            bottomSheetListDialog.setOnShowListener {
                bottomSheetListView.bottomsheetMapList.scrollToPosition(0)
            }
        }
        binding.btnFavoriteList.setOnClickListener {
            Log.d(TAG, "mainViewModel.pharmacies = ${mainViewModel.pharmacies.value}")
            bottomSheetListView.mapListTitle.text = "즐겨찾기한 약국"
            mainViewModel.getPharmacyList()
            pharmacyListRvAdapter.submitList(mainViewModel.pharmacies.value?.toList()?: listOf())
            bottomSheetListDialog.show()
            bottomSheetListDialog.setOnShowListener {
                bottomSheetListView.bottomsheetMapList.scrollToPosition(0)
            }
        }
        bottomSheetListView.closeBottomSheetList.setOnClickListener {
            bottomSheetListDialog.cancel()
        }
        // 바텀시트 리스트 다이얼로그를 닫게되면 데이터를 초기화함
        bottomSheetListDialog.setOnCancelListener {
            pharmacyListRvAdapter.submitList(listOf())
        }
        /*
        해시키 발급하는 키
        try {
            val keystore = KeyStore.getInstance("PKCS12")
            val keystoreFile = FileInputStream("alias 경로")
            val password = "비밀번호".toCharArray()

            keystore.load(keystoreFile, password)
            val cert: Certificate = keystore.getCertificate("alias 파일명")
            val md: MessageDigest = MessageDigest.getInstance("SHA-1")
            val publicKey = md.digest(cert.encoded)
            val base64 = Base64.getEncoder().encodeToString(publicKey)

            println("Key Hash: $publicKey")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        */
    }

    // 내 위치로 이동
    private fun kakaoMapMoveCamera(y: Double,x: Double) {
        var cameraUpdate = CameraUpdateFactory.newCenterPosition(LatLng.from(y, x))
        viewPlaceLongtudeX = myPlaceLongtudeX
        viewPlaceLatitudeY = myPlaceLatitudeY
        kakaoMapCall.moveCamera(cameraUpdate)
    }



    // 사용자에게 권한 요청 후 결과에 대한 처리 로직
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == REQUEST_PERMISSION_LOCATION) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                gpsInit()
//            } else {
//                Log.d(TAG, "onRequestPermissionsResult() _ 권한 허용 거부")
//                Toast.makeText(requireContext(), "권한이 없어 해당 기능을 실행할 수 없습니다.", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    // NEW GPS CODE
    fun gpsInit() {
        // 사용자의 위치를 얻을 때는 LocationManager라는 시스템 서비스를 이용
        locationManager  = requireContext().getSystemService(LOCATION_SERVICE) as LocationManager
        // 현재 기기에 어떤 위치 제공자가 있는지를 알고 싶다면 LocationManager의 allProviders 프로퍼티를 이용
        var result = "All Providers : "
        val providers = locationManager.allProviders
        for (provider in providers) {
            result += " $provider. "
        }
        Log.d(TAG, result)  // All Providers : passive, gps, network..
        // 지금 사용할 수 있는 위치 제공자를 알아보려면 getProviders() 함수를 이용
        result = "Enabled Providers : "
        val enabledProviders = locationManager.getProviders(true)
        for (provider in enabledProviders) {
            result += " $provider. "
        }
        Log.d(TAG, result)  // Enabled Providers : passive, gps, network..

        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                // 위치 정보가 변경될 때 호출되는 콜백
                myPlaceLongtudeX = location.longitude
                myPlaceLatitudeY = location.latitude
                viewPlaceLongtudeX = myPlaceLongtudeX
                viewPlaceLatitudeY = myPlaceLatitudeY
                // 위치 정보를 사용하여 원하는 작업 수행
                Log.e(TAG, "onLocationChanged ( myPlaceLongtudeX = ${location.latitude}, myPlaceLatitudeY = ${location.longitude} )")
                // 최초 한번 실행
                if (!locationStatus) {
                    if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        locationStatus = true
                        kakaoMapMoveCamera(myPlaceLatitudeY, myPlaceLongtudeX)
                        mapViewModel.findPharmacy(myPlaceLongtudeX, myPlaceLatitudeY)
                        mapViewModel.moreFindPharmacy(myPlaceLongtudeX, myPlaceLatitudeY)
                    }
                }

            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                // 위치 제공자 상태 변경 시 호출되는 콜백
                Log.e(TAG, "onStatusChanged")
            }

            override fun onProviderEnabled(provider: String) {
                // 위치 제공자가 사용 가능할 때 호출되는 콜백
                Log.e(TAG, "onProviderEnabled")
            }

            override fun onProviderDisabled(provider: String) {
                // 위치 제공자가 사용 불가능할 때 호출되는 콜백
                Log.e(TAG, "onProviderDisabled")
            }
        }
        // 위치 업데이트 요청
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 5f, locationListener)
        }

        // 위치정보얻기
       val location: Location? = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
       Log.d(TAG, LocationManager.NETWORK_PROVIDER)
       location?.let{
           myPlaceLongtudeX = location.longitude // 갱신 된 경도 127.11547410533494
           myPlaceLatitudeY = location.latitude // 갱신 된 위도 37.40754692649233
           val accuracy = location.accuracy
           val time = location.time
           Log.d(TAG, "$myPlaceLongtudeX, $myPlaceLatitudeY, $location, $accuracy, $time")
       } ?: run {
           // 위치 정보가 없을 경우 처리
           Log.d(TAG, "최근까지의 위치 정보를 가져올 수 없습니다.")
       }
    }

    // 카카오맵
    private fun kakaoMapInit() {
        // 카카오맵 실행
        Log.d(TAG, "myPlaceX, myPlaceY = $myPlaceLongtudeX, $myPlaceLatitudeY")
        KakaoMapUtil(requireContext()).kakaoMapInit(binding.mapView, mapViewModel) { kakaoMap ->
            kakaoMapCall = kakaoMap
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // 권한이 있을 때만 GPS 초기화
                gpsInit()
            }

            // 버튼 이벤트 설정
            // 버튼 이벤트를 통해 현재 위치 찾기
            binding.btnLocation.setOnClickListener {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    // 위치 찍는 코드
                    // startLocationUpdates()
                    // 현재 위치로 이동합니다.
                    kakaoMapMoveCamera(myPlaceLatitudeY, myPlaceLongtudeX)
                }
            }
            // 약국의 데이터가 들어오면 라벨을 찍어준다



            mapViewModel.items.observe(viewLifecycleOwner) { items ->
                labelStyle(items)
            }
            mainViewModel.pharmacies.observe(viewLifecycleOwner) {
                labelStyle(mapViewModel.items.value!!)
            }
        }
    }

    // label style set

    // label style set end

    private fun labelStyle(items: List<KakaoPlacePharmacy>) {
        // draw labels init
        kakaoMapCall.labelManager?.removeAllLabelLayer()
        kakaoMapCall.labelManager?.layer
        kakaoMapCall.labelManager?.lodLayer
        val styleClicked = kakaoMapCall.labelManager?.addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.icon_pharmacy_label_clicked)))
        val styleFavorite = kakaoMapCall.labelManager?.addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.icon_pharmacy_label_favorite)))
        val styleDefault = kakaoMapCall.labelManager?.addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.icon_pharmacy_label)))
        fun styleCheck (id: String): LabelStyles? {
            return if (LocalDataSource(requireContext()).isPharmacyChecked(id)) {
                styleFavorite
            } else {
                styleDefault
            }
        }
        fun pharmacyChecked(thisData: KakaoPlacePharmacy): Boolean = LocalDataSource(requireContext()).isPharmacyChecked(thisData.id)
        fun selectIconType(iconType: Boolean) {
            when (iconType) {
                false -> bottomSheetDetailView.btnFavorite.setImageResource(R.drawable.icon_favorite)
                true -> bottomSheetDetailView.btnFavorite.setImageResource(R.drawable.icon_favorite_active)
            }
        }
        var style: LabelStyles? = null
        var options: LabelOptions? = null
        // 사용자 커스텀으로 LabelLayer 생성
        val layer: LabelLayer? = kakaoMapCall.labelManager?.addLayer(
            LabelLayerOptions.from("default")
                .setOrderingType(OrderingType.Rank)
                .setCompetitionUnit(CompetitionUnit.IconAndText)
                .setCompetitionType(CompetitionType.None)
        )
        val layerFavorite: LabelLayer? = kakaoMapCall.labelManager?.addLayer(
            LabelLayerOptions.from("favorite")
                .setOrderingType(OrderingType.Rank)
                .setCompetitionUnit(CompetitionUnit.IconAndText)
                .setCompetitionType(CompetitionType.None)
        )
        layer?.zOrder = 10001
        layerFavorite?.zOrder = 10100
        fun labelDraw (item: KakaoPlacePharmacy) {
            // id가 이미 pharmacy에 있을 때
            style = styleCheck(item.id)
            // 라벨 옵션 지정. 위경도와 스타일 넣기
            options = LabelOptions.from(LatLng.from(item.y.toDouble(), item.x.toDouble())).setStyles(style).setTag(item.id).setClickable(true)
            // 레이어에 라벨 추가
            if (pharmacyChecked(item)) {
                // 이 아이템이 즐겨찾기에 있을 때
                layerFavorite?.addLabel(options!!)
            } else {
                // 이 아이템이 즐겨찾기에 없을 때
                layer?.addLabel(options!!)
            }
        }
        // 라벨찍기
        for (i in items) {
            labelDraw(i)
        }
        kakaoMapCall.setOnLabelClickListener { kakaoMap, labelLayer, label ->
            val pharmacyItem = items.first { it.id == label.tag }
            with(bottomSheetDetailView) {
                distance.text = "${pharmacyItem.distance}m"
                placeName.text = pharmacyItem.placeName
                phone.text = pharmacyItem.phone
                addressName.text = pharmacyItem.addressName
                roadAddressName.text = pharmacyItem.roadAddressName
                label.changeStyles(styleClicked)
                bottomSheetDetailDialog.setOnCancelListener {
                    style = styleCheck(pharmacyItem.id)
                    label.changeStyles(style)
                }
                // init icon option
                selectIconType(pharmacyChecked(pharmacyItem))
                btnFavorite.setOnClickListener {
                    if (pharmacyChecked(pharmacyItem)) {
                        mainViewModel.cancelFavoritePharmacy(pharmacyItem.id)
                    } else {
                        mainViewModel.addToFavoritePharmacyList(pharmacyItem)
                    }
                    selectIconType(pharmacyChecked(pharmacyItem))
                }
                detailBtnCall.setOnClickListener {
                    val phone = pharmacyItem.phone
                    if (phone.isNotEmpty()) {
                        val intent = Intent(Intent.ACTION_DIAL)
                        intent.data = Uri.parse("tel:$phone")
                        startActivity(intent)
                    } else {
                        Toast.makeText(context, "유효한 전화번호가 아닙니다.", Toast.LENGTH_SHORT).show()
                    }
                }
                detailBtnFindRoad.setOnClickListener {
                    val url = pharmacyItem.placeUrl
                    if (url.isNotEmpty()) {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(url)
                        startActivity(intent)
                    } else {
                        Toast.makeText(context, "유효한 URL이 아닙니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            bottomSheetDetailDialog.show()
            true
        }

        binding.mapResearch.setOnClickListener {
            val camera = kakaoMapCall.getCameraPosition()
            kakaoMapCall.labelManager?.removeAllLabelLayer()
            viewPlaceLatitudeY = camera?.position?.latitude?: 0.0
            viewPlaceLongtudeX = camera?.position?.longitude?: 0.0
            mapViewModel.findPharmacy(viewPlaceLongtudeX, viewPlaceLatitudeY)
        }

        binding.mapViewMore.setOnClickListener {
            if (mapViewModel.pharmacyPager <= 5) {
                mapViewModel.moreFindPharmacy(viewPlaceLongtudeX, viewPlaceLatitudeY)
            } else {
                Toast.makeText(requireContext(), "표시된 약국이 많아 더 이상 검색할 수 없습니다.", Toast.LENGTH_SHORT).show()
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
        locationManager.removeUpdates(locationListener)
    }


}



