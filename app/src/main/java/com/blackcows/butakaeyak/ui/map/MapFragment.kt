package com.blackcows.butakaeyak.ui.map

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.blackcows.butakaeyak.BuildConfig
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.databinding.FragmentMapBinding
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.KakaoMapSdk
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapType
import com.kakao.vectormap.MapView
import com.kakao.vectormap.MapViewInfo

class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val mapViewModel = ViewModelProvider(this).get(MapViewModel::class.java)
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        val root: View = binding.root
        KakaoSdk.init(requireContext(), BuildConfig.NATIVE_APP_KEY)
        val keyHash = Utility.getKeyHash(requireContext())
        Log.d("제발찍혀주세요키해쉬님부탁드립니다.", keyHash)
        return root
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.resume() // MapView 의 resume 호출
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.pause() // MapView 의 pause 호출
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // KakaoMap SDK 초기화
        KakaoMapSdk.init(requireContext(), BuildConfig.NATIVE_APP_KEY)
        val mapView: MapView = binding.mapView
        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                // 지도 API 가 정상적으로 종료될 때 호출됨
                Log.d("k3fWrite", "인증_실패_및_지도_사용_중_에러가_발생할_때_호출됨")
            }

            override fun onMapError(error: Exception) {
                // 인증 실패 및 지도 사용 중 에러가 발생할 때 호출됨
                Log.d("k3fWrite", "인증_실패_및_지도_사용_중_에러가_발생할_때_호출됨 : $error")
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                // 인증 후 API가 정상적으로 실행될 때 호출됨
                Log.d("k3fWrite", "인증_후_API가_정상적으로_실행될_때_호출됨")
            }

            override fun getPosition(): LatLng {
                // 지도 시작 시 위치 좌표를 설정
                return LatLng.from(37.406960, 127.115587)

            }

            override fun getZoomLevel(): Int {
                // 지도 시작 시 확대/축소 줌 레벨 설정
                return 15
            }

            override fun getMapViewInfo(): MapViewInfo {
                // 지도 시작 시 App 및 MapType 설정
                return MapViewInfo.from("")
            }

            override fun getViewName(): String {
                // KakaoMap의 고유한 이름을 설정
                return "MyFirstMap"
            }

            override fun isVisible(): Boolean {
                // 지도 시작 시 visible 여부를 설정
                return true
            }

            override fun getTag(): String {
                // KakaoMap의 tag을 설정
                return "FirstMapTag"
            }
        })
    }

}