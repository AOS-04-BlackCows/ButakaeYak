package com.example.yactong

import com.example.yactong.data.repository.DrugRepository
import com.example.yactong.data.retrofit.ApiBaseUrl
import com.example.yactong.data.retrofit.RetrofitClient
import com.example.yactong.data.save_raw.AiApiService
import com.example.yactong.data.save_raw.AiRetrofitClient
import com.example.yactong.data.save_raw.makeAiMessage
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
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


    @Test
    fun getSummarize() = runBlocking {
        val instance = AiRetrofitClient.getInstance().create(AiApiService::class.java)
        val result = suspendCoroutine { continuation ->


            drugRepository.searchDrugs("타이레놀") { drugs ->
                // callback 내용 작성
                continuation.resume(drugs)

            }
        }

        val response = instance.getSummarize(makeAiMessage(result))
        println(response.toString())

        assertNotNull(result)
        assertTrue(result.isNotEmpty())

        result.forEachIndexed { i, it ->
            println("$i: ${it.name}")
        }
    }
}