package com.blackcows.butakaeyak.firebase

import com.blackcows.butakaeyak.data.models.MyPharmacy
import com.blackcows.butakaeyak.data.source.api.MedicineInfoDataSource
import com.blackcows.butakaeyak.domain.repo.MedicineGroupRepository
import com.blackcows.butakaeyak.domain.repo.MemoRepository
import com.blackcows.butakaeyak.domain.repo.MyPharmacyRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class MyPharmacyRepositoryTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    @Inject
    lateinit var myPharmacyRepository: MyPharmacyRepository

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
    fun add() = runBlocking {
        val newOne = MyPharmacy(
            pharmacyId = "explicari",
            userId = "BLWfdGYaKKLhTC5TImTI",
            placeName = "Lindsey Ellison",
            phone = "(984) 200-4070",
            placeUrl = "http://www.bing.com/search?q=neque",
            addressName = "Leonor Griffin",
            roadAddressName = "Aurora Powell"
        )

        myPharmacyRepository.addToFavorite(newOne)
    }

    @Test
    fun getMy() = runBlocking {
        val userId = "BLWfdGYaKKLhTC5TImTI"
        val result = myPharmacyRepository.getMyFavorites(userId)

        println(result.size)

        result.forEach {
            println(it.placeName)
        }
    }

    @Test
    fun remove() = runBlocking {
        val userId = "BLWfdGYaKKLhTC5TImTI"
        val pharmacy = myPharmacyRepository.getMyFavorites(userId)[0]

        myPharmacyRepository.cancelFavorite(userId, pharmacy)
    }


}