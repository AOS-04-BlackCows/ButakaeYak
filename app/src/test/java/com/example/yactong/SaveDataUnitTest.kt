package com.example.yactong

import com.example.yactong.data.repository.DrugRepository
import com.example.yactong.data.retrofit.AiResultDto
import com.example.yactong.data.retrofit.Medicine
import com.example.yactong.data.save_raw.AiApiService
import com.example.yactong.data.save_raw.AiRequestDto
import com.example.yactong.data.save_raw.AiRetrofitClient
import com.example.yactong.data.save_raw.makeAiMessage
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.lang.reflect.Type
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class SaveDataUnitTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var drugRepository: DrugRepository

    @Before
    fun setUp() {
        hiltRule.inject()
        println("--------------------------------------------------------------------------------------")
    }

    @After
    fun after() {
        //println("--------------------------------------------------------------------------------------")
    }

    @Test
    fun toJson() {

    }


    @Test
    fun getSummarize() = runBlocking {
        val instance = AiRetrofitClient.getInstance().create(AiApiService::class.java)
        val result = suspendCoroutine { continuation ->


            drugRepository.searchDrugs("타이레놀") { drugs ->
                // callback 내용 작성
                continuation.resume(drugs)
            }
        }

        val list = listOf(makeAiMessage(result))
        val response = instance.getSummarize(AiRequestDto(list))
        val medicineJson = response.choice[0].msg.medicineJson

        val gson = Gson()
        val type: Type = object : TypeToken<List<Medicine>>() {}.type
        val medicines: List<Medicine> = gson.fromJson(medicineJson, type)

        medicines.forEachIndexed { i, it->
            println("$i: ${it.name}")
            println(it.effect)
            println(it.caution)
            println(it.warning)
            println(it.enterprise)
            println(it.sideEffect)
            println(it.storingMethod)
            println(it.instructions)
            println(it.interaction)
            println("--------------------------------------------------------------")
        }
    }
}