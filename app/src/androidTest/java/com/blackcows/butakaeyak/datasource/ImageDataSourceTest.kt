package com.blackcows.butakaeyak.datasource

import android.R
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.blackcows.butakaeyak.data.source.firebase.ImageDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.robolectric.Robolectric
import javax.inject.Inject


@HiltAndroidTest
class ImageDataSourceTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    @Inject
    lateinit var imageDataSource: ImageDataSource

    @ApplicationContext
    lateinit var context: Context

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

    @Test
    fun uploadTest() = runBlocking {
        val bmp1 = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888)
        imageDataSource.uploadProfile("test", bmp1)
    }

    @Test
    fun getHttpUrl() = runBlocking {
        val url = imageDataSource.getHttpUrl("test")

        println(url)
    }

    @Test
    fun deleteProfileTest() = runBlocking {
        val id = "test"
        imageDataSource.deleteProfile(id)
    }
}