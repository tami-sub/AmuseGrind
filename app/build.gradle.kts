plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.example.amusegrind"
    compileSdk = rootProject.extra.get("compileSdk") as Int

    defaultConfig {
        applicationId = "com.example.amusegrind"
        minSdk = rootProject.extra.get("minSdk") as Int
        targetSdk = rootProject.extra.get("targetSdk") as Int
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
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.orNull
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Compose
    implementation(libs.compose.activity)
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.compose.material)
    implementation(libs.compose.runtime)
    implementation(libs.compose.navigation)
    implementation(libs.compose.hilt.navigation)
    implementation(libs.compose.foundation)

    //Firebase
    implementation(libs.firebase.common.ktx)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.analytics)

    // Hilt
    implementation(libs.hilt.library)
    implementation(libs.dagger)
    implementation(libs.hilt.work)
    kapt(libs.hilt.compiler)
    kapt(libs.hilt.android.compiler)
    kapt(libs.dagger.compiler)

    implementation(libs.androidx.work.runtime)
    implementation(libs.kotlinx.serialization)

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(platform(libs.compose.bom))
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.test)
    debugImplementation(libs.compose.ui.test.manifest)

    // Modules
    implementation(project(":core"))
    implementation(project(":network"))
    implementation(project(":navigator"))
    implementation(project(":features:auth"))
}
