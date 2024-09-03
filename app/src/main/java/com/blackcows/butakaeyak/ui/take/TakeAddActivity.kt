package com.blackcows.butakaeyak.ui.take

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.databinding.ActivityTakeAddBinding

class TakeAddActivity : AppCompatActivity() {
    private lateinit var binding : ActivityTakeAddBinding

    //viewPager 설정
    private lateinit var viewPager : ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTakeAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        viewPager = binding.vp2Add
        binding.apply {
            if (viewPager.adapter == null) {
                vp2Add.adapter = TakeViewPagerAdapter(this@TakeAddActivity)
                vp2Add.isUserInputEnabled = false
            }
        }
    }

    // 다음 fragment로 이동
    fun moveToNextPage() {
        viewPager.currentItem = viewPager.currentItem + 1
    }

    // 이전 fragment로 이동
    fun moveToPreviousPage() {
        if (viewPager.currentItem > 0) {
            viewPager.currentItem = viewPager.currentItem - 1
        }
    }
}