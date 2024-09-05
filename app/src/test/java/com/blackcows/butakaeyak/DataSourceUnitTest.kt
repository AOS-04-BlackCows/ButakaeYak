package com.blackcows.butakaeyak

import com.blackcows.butakaeyak.data.retrofit.ApiBaseUrl
import com.blackcows.butakaeyak.data.retrofit.DrugApiService
import com.blackcows.butakaeyak.data.retrofit.RetrofitClient
import com.blackcows.butakaeyak.data.source.firebase.MedicineDataSource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.delay
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

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class DataSourceUnitTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject lateinit var medicineDataSource: MedicineDataSource

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
    fun api() = runBlocking {
        val instance = RetrofitClient.getInstance(ApiBaseUrl.DrugInfoUrl)
        val retrofit = instance.create(DrugApiService::class.java)

        val result = retrofit.getDrugInfo("타이레놀")

        result.body.items.forEachIndexed { i, it ->
            println("$i: ${it.itemName}, ${it.itemImage?: "없음"}")
        }

        println("resultCode: ${result.header.resultCode}, item: ${result.body.items[0].itemName}")
    }

    @Test
    fun getMedicine(): Unit = runBlocking {
        val medicine = medicineDataSource.searchMedicinesByName("타이레놀")

        medicine.forEachIndexed { i, it->
            println("$i) ${it.name}")
        }
    }

    @Test
    fun getMedicineCallback() = runBlocking {
        val result = medicineDataSource.searchMedicinesByNames("마더스 타이레놀")

        assertNotNull(result)
        //assertTrue(result.isNotEmpty())

        result.forEachIndexed { i, it->
            println("$i) ${it.name}")
        }
    }
}