package com.blackcows.butakaeyak.firebase.auth

import android.app.Activity
import android.util.Log
import android.view.View
import android.widget.TextView
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import java.util.Timer
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class FirebaseAuthManager(
    private val smsVerificationListener: SmsVerificationListener
) {

    /**
     * 이 인증 과정은 FireBase가 사용자를 인증하는 과정임.
     * 따라서, 회원가입 뿐만 아니라 로그인을 할떄도 이 인증과정을 거쳐야함.
     * 로그아웃도 해줘야함.
     */

    /**
     * 입력된 전화 번호로 인증 시도.
     * @param phoneNumber: 인증 코드를 보낼 전화 번호
     */

    fun verifyPhoneNumber(context: Activity, phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(TIME_OUT, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(context) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    /**
     * 입력된 숫자 코드를 이용해 인증 시도
     * @param numberCode: 인증을 위해 입력된 숫자 코드
     */

    fun submitSmsCode(numberCode: String) {
        val credential = PhoneAuthProvider.getCredential(storedVerificationId, numberCode)
        signInWithPhoneAuthCredential(credential)
    }

    /**
     * onSuccess: 인증에 성공했을 때
     *
     * onFail: 인증에 실패했을 때
     *
     * @property timer: 남은 시간을 표시할 TextView
     */
    abstract class SmsVerificationListener(val timer: TextView) {
        abstract fun onSuccess()
        abstract fun onFail(e: Exception)
    }

    companion object {
        private const val TAG = "FirebaseAuthManager"
        private const val TIME_OUT = 60L        //60초 안에 SMS 코드 입력.

        /**
         *  Firebase에 로그인 되어 있나?
         */
        fun isSignIn(): Boolean {
            return Firebase.auth.currentUser != null
        }
        /**
         *  Firebase 로그아웃
         */
        fun signOut() {
            Firebase.auth.signOut()
        }
    }

    //----------------------------------------------------------------------------------------------------------
    // 이 아래는 안 봐도 됨
    //----------------------------------------------------------------------------------------------------------
    //Doc: https://firebase.google.com/docs/auth/android/phone-auth?hl=ko&authuser=0&_gl=1*r379bk*_ga*MTcxNjAxMjQ2MS4xNzI0ODIyMTAx*_ga_CW55HF8NVT*MTcyNDgzNDgzMS4yLjEuMTcyNDgzNTAxMy41MS4wLjA.


    private var storedVerificationId = ""

    private val auth = FirebaseAuth.getInstance().apply {
        setLanguageCode("KR")
    }


    private val callbacks = object: OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Log.d(TAG, "Verification Start When Instance Verification or Auto-Retriever.")
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Log.i(TAG, "Verification Failed.")
            finishTimer()
        }

        override fun onCodeAutoRetrievalTimeOut(p0: String) {
            super.onCodeAutoRetrievalTimeOut(p0)
            smsVerificationListener.onFail(TimeoutException("Time Out!"))
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            // 문자로 여섯 자리 숫자 코드를 보낸 뒤 호출되는 함수.
            Log.d(TAG, "Please Enter the Code.")
            storedVerificationId = verificationId

            startTimer()
        }

    }

    private var timer: Timer? = null
    private fun startTimer() {
        var secondsRemaining = TIME_OUT
        timer = Timer()
        timer!!.schedule(object : java.util.TimerTask() {
            override fun run() {
                if (secondsRemaining > 0) {
                    secondsRemaining--
                    CoroutineScope(Dispatchers.Main).launch {
                        smsVerificationListener.timer.text = secondsRemaining.toString()
                    }
                } else {
                    timer!!.cancel()
                }
            }
        }, 0, 1000)
    }
    private fun finishTimer() {
        timer?.cancel()
        timer = null
    }




    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                finishTimer()
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")

                    val user = task.result?.user
                    smsVerificationListener.onSuccess()
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.i(TAG, "signInWithCredential:failure", task.exception)
                    smsVerificationListener.onFail(task.exception?: java.lang.Exception())
                }
            }
    }
}