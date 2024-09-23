package com.blackcows.butakaeyak.ui.map

import android.content.Context
import android.util.Log
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.viewModelScope
import com.blackcows.butakaeyak.BuildConfig
import com.blackcows.butakaeyak.R
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.KakaoMapSdk
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.MapViewInfo
import com.kakao.vectormap.camera.CameraAnimation
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles

private const val TAG = "k3f_KakaoMapUtil"

class KakaoMapUtil (private val context: Context) {
    fun kakaoMapInit(mapView: MapView, mapViewModel: MapViewModel, onMapReady: (KakaoMap) -> Unit) {
        // KakaoMap SDK 초기화
        KakaoMapSdk.init(context, BuildConfig.NATIVE_APP_KEY)
        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                // 지도 API 가 정상적으로 종료될 때 호출됨
                Log.d(TAG, "인증_실패_및_지도_사용_중_에러가_발생할_때_호출됨")
            }

            override fun onMapError(error: Exception) {
                // 인증 실패 및 지도 사용 중 에러가 발생할 때 호출됨
                Log.d(TAG, "인증_실패_및_지도_사용_중_에러가_발생할_때_호출됨 : $error")
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                // 인증 후 API가 정상적으로 실행될 때 호출됨
                Log.d(TAG, "인증_후_API가_정상적으로_실행될_때_호출됨")
                onMapReady(kakaoMap)
            }

            override fun getPosition(): LatLng {
                // 지도 시작 시 위치 좌표를 설정
                return LatLng.from(0.0, 0.0)
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