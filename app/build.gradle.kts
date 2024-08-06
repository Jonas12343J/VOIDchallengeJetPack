import org.jetbrains.kotlin.gradle.internal.Kapt3GradleSubplugin.Companion.isIncludeCompileClasspath

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlin.serialization)
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.example.voidchallengejetpack"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.voidchallengejetpack"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.constraintlayout.compose)


    //androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)

    // Local unit tests
    testImplementation ("androidx.test:core:1.6.1")
    testImplementation ("junit:junit:4.13.2")
    testImplementation ("androidx.arch.core:core-testing:2.2.0")
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.1")
    testImplementation ("com.google.truth:truth:1.1.3")
    testImplementation ("com.squareup.okhttp3:mockwebserver:4.9.1")
    testImplementation ("io.mockk:mockk:1.10.5")
    testImplementation ("org.mockito:mockito-core:3.12.4")
    testImplementation ("org.mockito:mockito-inline:3.12.4") // for mocking final classes
    debugImplementation(libs.androidx.ui.test.manifest)

    // Instrumentation tests
    androidTestImplementation ("com.google.dagger:hilt-android-testing:2.37")
    kaptAndroidTest ("com.google.dagger:hilt-android-compiler:2.49")
    androidTestImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.1")
    androidTestImplementation ("androidx.arch.core:core-testing:2.2.0")
    androidTestImplementation ("com.google.truth:truth:1.1.3")
    androidTestImplementation ("androidx.test.ext:junit:1.2.1")
    androidTestImplementation ("androidx.test:core-ktx:1.6.1")
    androidTestImplementation ("com.squareup.okhttp3:mockwebserver:4.9.1")
    androidTestImplementation ("io.mockk:mockk-android:1.10.5")
    androidTestImplementation ("androidx.test:runner:1.6.1")


    implementation(libs.glide)
    implementation(libs.navigation.compose)
    implementation(libs.splashscreen)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter)
    implementation(libs.okhttp)
    implementation(libs.loggingInterceptor)

    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.runtime.ktx)

    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    implementation(libs.coil)
    implementation(libs.coil.compose)
    implementation(libs.accompanist.coil)

    implementation(libs.palette)

    implementation(libs.timber)


}

kapt {
    correctErrorTypes = true
}