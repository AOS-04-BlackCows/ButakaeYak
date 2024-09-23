package com.blackcows.butakaeyak

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import com.blackcows.butakaeyak.databinding.ActivityMainBinding
import com.blackcows.butakaeyak.ui.navigation.MainNavigation
import com.blackcows.butakaeyak.ui.state.LoginUiState
import com.blackcows.butakaeyak.ui.viewmodels.UserViewModel
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val REQUEST_PERMISSION_LOCATION = 10
    private val TAG = "MainActivity"

    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MainNavigation.initialize(this, binding)

        //TODO 알림 설정
        createNotificationChannel()
        // GPS 권한 체크 후 없을 시 요청
        checkPermissionForLocation()

        KakaoSdk.init(this, BuildConfig.NATIVE_APP_KEY)

        lifecycleScope.launch {
            userViewModel.loginUiState.collectLatest {
                when(it) {
                    is LoginUiState.Success -> {
                        MainNavigation.disableLoadingBar()
                    }

                    is LoginUiState.Loading -> {
                        MainNavigation.showLoadingBar()
                    }

                    is LoginUiState.NotFoundAutoLoginData -> {
                        MainNavigation.disableLoadingBar()
                    }

                    is LoginUiState.Failure -> {
                        MainNavigation.disableLoadingBar()
                        Toast.makeText(this@MainActivity, "로그인에 실패하였습니다. 다시 로그인해주세요.", Toast.LENGTH_SHORT).show()
                    }

                    else -> {
                        MainNavigation.disableLoadingBar()
                    }
                }
            }
        }


    }

    override fun onResume() {
        super.onResume()

        userViewModel.autoLogin()
    }

    //TODO 알림 설정
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Alarm Channel"
            val descriptionText = "Channel for Alarm Manager"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("alarm_channel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    // 위치 권한이 있는지 확인하는 메서드
    private fun checkPermissionForLocation(): Boolean {
        // Android 6.0 Marshmallow 이상에서는 위치 권한에 추가 런타임 권한이 필요
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                true
            } else {
                // 권한이 없으므로 권한 요청 알림 보내기
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSION_LOCATION)
                false
            }
        } else {
            true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 승인 gn tlfgod

            } else {
                Log.d(TAG, "onRequestPermissionsResult() _ 권한 허용 거부")
//                Toast.makeText(this, "권한이 없어 해당 기능을 실행할 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}