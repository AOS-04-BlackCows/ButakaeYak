package com.blackcows.butakaeyak.firebase

import com.blackcows.butakaeyak.data.models.User
import com.blackcows.butakaeyak.data.models.UserRequest
import com.blackcows.butakaeyak.data.toObjectsWithId
import com.blackcows.butakaeyak.domain.repo.UserRepository
import com.blackcows.butakaeyak.domain.result.LoginResult
import com.blackcows.butakaeyak.domain.result.SignUpResult
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
//@RunWith(RobolectricTestRunner::class)
//@Config(application = HiltTestApplication::class)
class UserRepositoryTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    @Inject
    lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        hiltRule.inject()
        println("--------------------------------------------------------------------------------------")
    }

    @After
    fun after() {
        println("--------------------------------------------------------------------------------------")
    }

    private val userRequest = UserRequest(
        name = "Jeffrey Meadows",
        loginId = "Jeffrey",
        pwd = "Jeffrey123",
        profileUrl = null,
        kakaoId = null,
        deviceToken = null
    )

    @Test
    fun dbTest() = runBlocking {
        val db = Firebase.firestore

        val user = db.collection("users")
            .whereEqualTo("name", userRequest.name)
            .get().await().toObjectsWithId<User>()

        user.forEach {
            println("name: ${it.name}")
        }
    }

    @Test
    fun integrate() = runBlocking {
        createUserTest()
        loginWithId()
    }

    @Test
    fun createUserTest() = runBlocking {
        val result = userRepository.signUpUserData(userRequest)
        when(result) {
            is SignUpResult.Success -> {
                println("Success: ${result.user.name}")
            }
            is SignUpResult.KakaoSignUpFail -> {
                println("KakaoSignUpFail")
            }
            is SignUpResult.LoginIdDuplicate -> {
                println("LoginIdDuplicate")
            }
            is SignUpResult.Failure -> {
                println("Failure")
            }
        }
    }

    @Test
    fun loginWithId() = runBlocking {
        val result = userRepository.loginWithId(userRequest.loginId!!, userRequest.pwd!!)
        when(result) {
            is LoginResult.Success -> {
                println("Success: ${result.user.id} ${result.user.name}")
                deleteUserTest(result.user)
            }
            is LoginResult.UnknownAccount -> {
                println("UnknownAccount")
            }
            is LoginResult.Failure -> {
                println("Failure")
            }
        }


    }

    fun deleteUserTest(user: User) = runBlocking {
        val result = userRepository.deleteAccount(user)
        println("delete!")
    }

    @Test
    fun uploadProfile() = runBlocking {
        val loginId = "Esmeralda"
        val pwd = "Esmeralda123"
        val result = userRepository.loginWithId(loginId, pwd)
        val user = when(result) {
            is LoginResult.Success -> {
                result.user
            }
             else -> {
                 println("Exception!")
                 return@runBlocking
             }
        }

        val image = "https://img1.kakaocdn.net/thumb/R110x110.q70/?fname=https://t1.kakaocdn.net/account_images/default_profile.jpeg"
        val newOne = userRepository.setProfile(user, image)

        println(newOne.profileUrl!!)
    }
}