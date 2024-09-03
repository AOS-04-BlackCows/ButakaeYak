package com.blackcows.butakaeyak.ui.take

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.databinding.ActivityTakeBinding

class TakeActivity : AppCompatActivity() {
    //binding
    private lateinit var binding : ActivityTakeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTakeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        binding.apply {
            btnAdd.setOnClickListener {
                val intent = Intent(this@TakeActivity,TakeAddActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.alpha,R.anim.none)
                finish()
            }
        }
    }
}