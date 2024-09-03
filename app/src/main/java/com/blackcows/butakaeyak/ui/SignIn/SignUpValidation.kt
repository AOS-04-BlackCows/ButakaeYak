package com.blackcows.butakaeyak.ui.SignIn

object SignUpValidation {
    // 010-xxxx-xxxx
    fun isValidPhoneNumber(phoneNumber: String): Boolean {
        val phoneNumberPattern = "^(010-\\d{4}-\\d{4}\$)".toRegex()
        return phoneNumberPattern.matches(phoneNumber)
    }

    // 한국어만 가능
    fun isValidName(name: String) : Boolean {
        val namePattern = "^([가-힣]+$)".toRegex()
        return namePattern.matches(name)
    }

    // 0보다 큰 숫자만 가능
    fun isValidAge(age: Int) : Boolean {
        return age > 0
    }

    // 영어, .!@#$만 가능
    fun isValidPw(pw : String) : Boolean {
        val pwPattern = "^([a-zA-Z.!@#$]+$)".toRegex()
        return pwPattern.matches(pw)
    }
}