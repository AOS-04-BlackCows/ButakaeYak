package com.blackcows.butakaeyak.ui.SignIn

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.set
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.databinding.ActivitySignInBinding
import com.blackcows.butakaeyak.firebase.firebase_store.FirestoreManager
import com.blackcows.butakaeyak.firebase.firebase_store.models.UserData
import com.google.firebase.firestore.auth.User

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private val firestoreManager = FirestoreManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        with(binding) {
            btnLogin.setOnClickListener {
                val userPhoneNumber = inputPhoneNumber.text.toString()

                firestoreManager.trySignIn(userPhoneNumber,
                    object : FirestoreManager.ResultListener<UserData> {
                        override fun onSuccess(result: UserData) {
                            // 회원가입 성공 이벤트
                            Toast.makeText(
                                this@SignInActivity,
                                "로그인 성공",
                                Toast.LENGTH_LONG
                            ).show()
                            //val intent = Intent(this@SignInActivity, MyPage::class.java)
                            //startActivity(intent)
                            finish()
                        }

                        override fun onFailure(e: Exception) {
                            //실시간으로 체크 Text Watcher
                        }

                    }
                )
            }

            val fullText = "계정이 없으신가요? 회원가입"
            val spannableString = SpannableString(fullText)

            val signUpStart = fullText.indexOf("회원가입")
            val signUpEnd = signUpStart + "회원가입".length

            // ForegroundColorSpan
            val foregroundColorSpan =
                ForegroundColorSpan(getColor(R.color.any_500))

            val signUpClickableSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    val intent = Intent(this@SignInActivity, SignUpActivity::class.java)
                    startActivity(intent)
                }
            }

            // span 적용
            spannableString.setSpan(signUpClickableSpan, signUpStart, signUpEnd, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            spannableString.setSpan(foregroundColorSpan, signUpStart, signUpEnd, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)

            // textView에 적용
            tvSignup.text = spannableString
            tvSignup.movementMethod = LinkMovementMethod.getInstance() // 클릭 가능하게 설정
        }
    }
}