package com.blackcows.butakaeyak.firebase

import com.blackcows.butakaeyak.data.models.Friend
import com.blackcows.butakaeyak.data.models.FriendRequest
import com.blackcows.butakaeyak.data.models.User
import com.blackcows.butakaeyak.data.models.UserRequest
import com.blackcows.butakaeyak.data.toMap
import com.blackcows.butakaeyak.domain.repo.FriendRepository
import com.blackcows.butakaeyak.domain.repo.UserRepository
import com.blackcows.butakaeyak.domain.result.LoginResult
import com.blackcows.butakaeyak.domain.result.SignUpResult
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class FriendRepositoryTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    @Inject
    lateinit var friendRepository: FriendRepository
    @Inject
    lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        hiltRule.inject()
        //FirebaseFirestore.setLoggingEnabled(false)
        println("--------------------------------------------------------------------------------------")
    }

    @After
    fun after() {
        println("--------------------------------------------------------------------------------------")
    }

    private lateinit var user1: User
    private lateinit var user2: User

    private val userRequest1 = UserRequest(
        name = "Esmeralda Leon",
        loginId = "Esmeralda",
        pwd = "Esmeralda123",
        profileUrl = null,
        kakaoId = null,
        deviceToken = null
    )
    private val userRequest2 = UserRequest(
        name = "Arturo Hendricks",
        loginId = "Arturo",
        pwd = "Arturo123",
        profileUrl = null,
        kakaoId = null,
        deviceToken = null
    )


    @Test
    fun integrate() = runBlocking {
        createUser()
        requestFriend()
        getMyPropose()
        get2Received()
        acceptFriend()
        getMyFriends()
        //cancelFriend()
        //deleteUser()
    }

    fun createUser() = runBlocking {
        val req1 = userRepository.signUpUserData(userRequest1)
        if(req1 is SignUpResult.Success) {
            user1 = req1.user
        } else {
            val log1 = userRepository.loginWithId(userRequest1.loginId!!, userRequest1.pwd!!)
            if(log1 is LoginResult.Success) {
                user1 = log1.user
            } else {
                throw Exception("wrong1: ${log1.toString()}")
            }
        }
        val req2 = userRepository.signUpUserData(userRequest2)
        if(req2 is SignUpResult.Success) {
            user2 = req2.user
        }else {
            val log2 = userRepository.loginWithId(userRequest2.loginId!!, userRequest2.pwd!!)
            if(log2 is LoginResult.Success) {
                user2 = log2.user
            } else {
                throw Exception("wrong1: ${log2.toString()}")
            }
        }
    }
    fun deleteUser() = runBlocking {
        userRepository.deleteAccount(user1)
        userRepository.deleteAccount(user2)
    }

    @Test
    fun requestFriend() = runBlocking {
        friendRepository.requestFriend(user1.id, user2.id)
    }

    @Test
    fun getMyPropose() = runBlocking {
        val list = friendRepository.getMyProposal(user1.id)
        list.forEach {
            println(it.toMap().toString())
        }
    }

    @Test
    fun get2Received() = runBlocking {
        val list = friendRepository.getMyReceived(user2.id)
        list.forEach {
            println(it.toMap().toString())
        }
    }

    @Test
    fun acceptFriend() = runBlocking {
        val list = friendRepository.getMyProposal(user1.id)
        if(list.isNotEmpty()) {
            friendRepository.acceptRequest(list[0])
        }
    }

    @Test
    fun getMyFriends() = runBlocking {
        val list = friendRepository.getMyFriends(user1.id)
        list.forEach {
            println("${it.proposer}, ${it.receiver}, ${it.isConnected}")
        }
    }

    @Test
    fun cancelFriend() = runBlocking {
        val list = friendRepository.getMyFriends(user1.id)
        if(list.isNotEmpty()) {
            friendRepository.disconnectFriend(list[0].proposer, list[0].receiver)
        }
    }
}