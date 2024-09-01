package com.example.yactong

import android.app.Activity
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.yactong.data.save_raw.SaveMedicineModule
import com.example.yactong.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import com.example.yactong.firebase.auth.FirebaseAuthManager
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject lateinit var saveMedicineModule: SaveMedicineModule

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: Editor

    private val APP_SHARED_PREFS = "SaveDataApplication"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        
        navView.setupWithNavController(navController)

        CoroutineScope(Dispatchers.IO).launch {
            saveMedicineModule.doSave(
                sharedPreferences, editor
            )
        }
    }

}