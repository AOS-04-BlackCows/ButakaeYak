import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    //hilt 추가 내용
    alias(libs.plugins.dagger.hilt.android)

    kotlin("plugin.serialization") version "1.5.0"

    id("kotlin-parcelize")
    id("kotlin-kapt")


    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.yactong"
    compileSdk = 34

    val properties = Properties()
    properties.load(project.rootProject.file("local.properties").inputStream())
    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.example.yactong"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.example.yactong.HiltTestRunner"

        buildConfigField("String",
            "DRUG_INFO_KEY",
            properties.getProperty("drug_info_key")
        )
        buildConfigField("String",
            "NATIVE_APP_KEY",
            properties.getProperty("native_app_key")
        )
        buildConfigField("String",
            "OPEN_AI_KEY",
            properties.getProperty("open_ai_key")
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    
    implementation(libs.androidx.activity)
    implementation(libs.firebase.firestore)
    
    implementation(libs.androidx.runner)
    
    implementation(libs.androidx.junit.ktx)
    
    implementation(libs.androidx.activity)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.fragment.ktx)
    
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    testImplementation(libs.androidx.runtime.android)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.glide)

    implementation(libs.androidx.lifecycle.livedata.ktx)

    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-android-compiler:2.47")
    // For Robolectric tests.
    testImplementation("com.google.dagger:hilt-android-testing:2.48")
    kaptTest("com.google.dagger:hilt-android-compiler:2.47")
    // For instrumented tests.
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.48")
    // ...with Kotlin.
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.47")

    testImplementation("org.robolectric:robolectric:4.9")

    // LiveData (optional)
    implementation("androidx.lifecycle:lifecycle-livedata-ktx")

    // Coroutine (for StateFlow)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android")

    // 카카오 지도 SDK
    implementation("com.kakao.sdk:v2-all:2.20.5")
    implementation("com.kakao.maps.open:android:2.11.9")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.2.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth-ktx")     //인증
}