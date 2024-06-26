// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.kotlin.serialization)
}

buildscript {
    extra.apply {
        set("compileSdk", 34)
        set("minSdk", 29)
        set("targetSdk", 34)
    }

    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("org.jlleitschuh.gradle:ktlint-gradle:12.1.0")
    }
}

allprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        debug.set(false)
        verbose.set(false)
    }
}
