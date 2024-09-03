package com.blackcows.butakaeyak.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.blackcows.butakaeyak.MainActivity
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.databinding.ActivityLoadingBinding
import java.util.Timer
import kotlin.concurrent.timer

class LoadingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoadingBinding

    var timer : Timer? = null
    var deltaTime = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        TimerFun()
    }

    fun TimerFun() {
        // 0.1초에 1%씩 증가, 시작 버튼 누른 후 3초 뒤 시작
        timer = timer(period = 30, initialDelay = 2000) {
            if(deltaTime >= 100) {
                cancel()
                val intent = Intent(this@LoadingActivity, MainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.alpha,R.anim.none)
                finish()
            }
            else {
                runOnUiThread {
                    binding.progressBar.setProgress(++deltaTime)
                    println(binding.progressBar.progress)
                }
            }
        }
    }
}