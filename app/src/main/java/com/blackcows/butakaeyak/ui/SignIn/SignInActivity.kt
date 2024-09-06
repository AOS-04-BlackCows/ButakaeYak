package com.blackcows.butakaeyak.ui.SignIn

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.blackcows.butakaeyak.BuildConfig
import com.blackcows.butakaeyak.R
import com.blackcows.butakaeyak.databinding.ActivitySignInBinding
import com.blackcows.butakaeyak.firebase.firebase_store.FirestoreManager
import com.blackcows.butakaeyak.firebase.firebase_store.models.UserData
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient


class SignInActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySignInBinding
    private val firestoreManager = FirestoreManager()
    private val TAG = "SignInActivity"

    // 이메일 로그인 콜백
    private val mCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (token != null) {
            Log.e(TAG, "로그인 성공${token.accessToken}")
            Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show()
            kakaoLogin()

            firestoreManager.saveKakaoUser(object :
                FirestoreManager.ResultListener<Boolean> {
                override fun onSuccess(result: Boolean) {
                    Log.d(TAG, "Firebase 저장 성공")
                }

                override fun onFailure(e: Exception) {
                    Log.e(TAG, "Firebase저장 실패", e)
                }
            })

        } else {
            if (error != null) {
                Log.e(TAG, "카카오 계정으로 로그인 실패!! error : ${error.message}")
            }
            Toast.makeText(this, "알 수 없는 이유로 로그인 실패하셨습니다. 다시 시도해주세요.", Toast.LENGTH_LONG).show()
        }
        kakaoLogin()
    }


    // 데이터 전달
    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            val id = result.data?.getStringExtra("id") ?: "none"
            val pw = result.data?.getStringExtra("pw") ?: "none"

            binding.inputId.setText(id)
            binding.inputPw.setText(pw)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        setSignUpTextView()

        Log.d(TAG, Utility.getKeyHash(this))

        KakaoSdk.init(this, BuildConfig.NATIVE_APP_KEY)

        binding.ivKakao.setOnClickListener(this)

        val inPutId = intent.getStringExtra("id")
        val inPutPw = intent.getStringExtra("pw")

        binding.inputId.setText(inPutId)
        binding.inputPw.setText(inPutPw)


    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.ivKakao.id -> {

                // 카카오톡 설치 확인
                // 설치 true -> 카카오톡 로그인
                // 설치 false -> 카카오 이메일 로그인
                // 로그인 실패하게 된다면 if문 타고 사용자가 취소했을 때는 그대로 return하여 login이 취소되고 -> 사용자가 취소한거 아니면 이메일 로그인
                if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                    // 카카오톡 로그인
                    UserApiClient.instance.loginWithKakaoTalk(this, callback = mCallback)
                } else {
                    // 카카오 이메일 로그인
                    UserApiClient.instance.loginWithKakaoAccount(this, callback = mCallback)
                }
            }
        }
    }

    private fun initView() {
        with(binding) {
            btnLogin.setOnClickListener {
                val userId = inputId.text.toString()
                val name = inputId.text.toString()

                val pw = inputPw.text.toString()
                if (validateInputs(name, pw)) {
                    firestoreManager.trySignIn(userId,
                        object : FirestoreManager.ResultListener<UserData> {
                            override fun onSuccess(result: UserData) {
                                // 회원가입 성공 이벤트
                                Toast.makeText(
                                    this@SignInActivity,
                                    "로그인 성공",
                                    Toast.LENGTH_LONG
                                ).show()
                                Log.d(TAG, "로그인 페이지로 이동")

                                // 로그인에 아이디 & 비밀번호 전달
                                val intent = Intent().apply {
                                    putExtra("userData", result)
                                }
                                setResult(RESULT_OK, intent)
                                finish()
                            }

                            override fun onFailure(e: Exception) {
                                Toast.makeText(
                                    this@SignInActivity,
                                    "로그인 실패: ${e.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        })
                } else {
                    Toast.makeText(
                        this@SignInActivity,
                        "아이디와 비밀번호를 입력해주세요.",
                        Toast.LENGTH_LONG
                    ).show()
                }
                Log.d(TAG, "아이디와 비밀번호 입력해주세요. ")
            }
        }
    }

    private fun validateInputs(id: String, password: String): Boolean {
        return id.isNotEmpty() && password.isNotEmpty()
    }

    private fun setSignUpTextView() {

        // TextView 참조
        val textSignUp: TextView = binding.tvSignup

        // 전체 텍스트 설정
        val fullText = "계정이 없으신가요? 회원가입"
        val spannableString = SpannableString(fullText)

        // "회원가입" 텍스트의 시작 위치와 끝 위치 계산
        val signUpStart = fullText.indexOf("회원가입")
        val signUpEnd = signUpStart + "회원가입".length
        Log.d(TAG, "회원가입 시작 위치: $signUpStart, 끝 위치: $signUpEnd")

        // "회원가입" 텍스트에 클릭 이벤트 설정
        val signUpClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                Log.d(TAG, "회원가입 텍스트 클릭됨")
                val intent = Intent(this@SignInActivity, SignUpActivity::class.java)
                resultLauncher.launch(intent)
            }
        }
        // Span에 적용
        spannableString.setSpan(
            signUpClickableSpan,
            signUpStart,
            signUpEnd, Spanned.SPAN_INCLUSIVE_INCLUSIVE
        )

        // 색상 설정
        val signUpColor =
            ForegroundColorSpan(ContextCompat.getColor(this@SignInActivity, R.color.back_700))
        Log.d(TAG, "회원가입 텍스트 색상: $signUpColor")
        // Span 적용
        spannableString.setSpan(
            signUpColor,
            signUpStart,
            signUpEnd,
            Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        )

        // TextView에 적용
        textSignUp.movementMethod = LinkMovementMethod.getInstance() // 클릭 가능하게 설정
        textSignUp.text = spannableString
    }

    private fun kakaoLogin() {
        UserApiClient.instance.me { user, meError ->
            if (meError != null) {
                Log.e(TAG, "사용자 정보 요청 실패 : $meError")
            } else if (user != null) {
                Log.d(TAG, "사용자 정보 요청 성공 : $user")
                val intent = Intent().apply {
                    putExtra(
                        "name",
                        user.kakaoAccount?.profile?.nickname
                    )
                    putExtra("thumbnail", user.kakaoAccount?.profile?.thumbnailImageUrl)
                }
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }
}
