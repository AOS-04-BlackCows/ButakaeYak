package com.blackcows.butakaeyak

import com.blackcows.butakaeyak.domain.repo.DrugRepository
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
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

//TODO: Java 17 is Required to Run this Code.

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class RepositoryUnitTest {
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
        println("--------------------------------------------------------------------------------------")
    }

    @Test
    fun apiDrugTest() = runBlocking {
        val result = suspendCoroutine { continuation ->
            drugRepository.searchDrugs("한미아스피린") { drugs ->
                // callback 내용 작성
                continuation.resume(drugs)
            }
        }

        assertNotNull(result)
        assertTrue(result.isNotEmpty())

        result.forEachIndexed { i, it ->
            println("$i: ${it.name}")
        }
    }



    @Test
    fun apiPillTest() = runBlocking {
        val result = suspendCoroutine { continuation ->
            drugRepository.searchPills("타이레놀") { pills ->
                // callback 내용 작성
                continuation.resume(pills)
            }
        }

        assertNotNull(result)
        //assertTrue(result.isNotEmpty())

        result.forEachIndexed { i, it ->
            println("$i: ${it.name}")
        }
    }
}