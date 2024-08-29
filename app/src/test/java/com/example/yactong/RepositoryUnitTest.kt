package com.example.yactong

import com.example.yactong.data.repository.DrugRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject

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
    }

    @Test
    fun apiDrugTest() = runBlocking {
        val drugs = drugRepository.searchDrugs("한미아스피린").onFailure {
            println("Loading Drugs is Failed) ${it.message}")
        }.onSuccess {
            println("----------------------------------------------")
            it.forEachIndexed { i, it ->
                println("$i: ${it.name}, ${it.imageUrl?: "없음"}")
            }
        }
    }

    @Test
    fun apiPillTest() = runBlocking {
        val pills = drugRepository.searchPills("타이레놀").onFailure {
            println("Loading Pills is Failed...")
        }.onSuccess {
            println("----------------------------------------------")
            it.forEachIndexed { i, it ->
                println("$i: ${it.name}")
            }
            println("----------------------------------------------")
        }
    }
}