// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.ksp) apply false
}

buildscript {
    extra.apply {
        set("compileSdk", 34)
        set("minSdk", 28)
        set("targetSdk", 34)
    }

    dependencies {
        classpath(libs.ktlint.gradle)
        classpath(libs.hilt.plugin.gradle)
    }
}