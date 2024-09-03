package com.blackcows.butakaeyak.ui.SignIn

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.blackcows.butakaeyak.databinding.ActivitySignUpBinding
import com.blackcows.butakaeyak.firebase.firebase_store.FirestoreManager
import com.blackcows.butakaeyak.firebase.firebase_store.models.UserData

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val firestoreManager = FirestoreManager()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        with(binding) {
            btnSignup.setOnClickListener {
                var userDate = UserData("", 0, "")
                firestoreManager.trySignUp(userDate,
                    object : FirestoreManager.ResultListener<Boolean> {
                        override fun onSuccess(result: Boolean) {
                            // 회원가입 성공 이벤트
                            Toast.makeText(
                                this@SignUpActivity,
                                "회원가입 성공",
                                Toast.LENGTH_LONG
                            ).show()
                            // 로그인에 전화번호 & 비밀번호 전달
                            val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
                            startActivity(intent)
                            finish()
                        }

                        override fun onFailure(e: Exception) {
                            //실시간으로 체크 Text Watcher
                        }

                    }
                )
            }

            fun formatPhoneNumber(phoneNumber : String, isDeleting : Boolean) : String {
                // 숫자만 남기기
                var digits = phoneNumber.replace(Regex("\\D"), "")
                if (isDeleting && digits.isNotEmpty()) {
                    digits = digits.substring(0, digits.length -1)
                }
                return when {
                    digits.length <= 3 -> digits
                    digits.length <= 7 -> "010-${digits.substring(0, 4)}-${digits.substring(4)}"
                    else -> "010-${digits.substring(0, 4)}-${digits.substring(4, 8)}"
                }
            }
        }
    }
}