plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-android")
}

dependencies {
    implementation(project(":MultiPlatformLibrary"))
    implementation("com.google.android.material:material:1.3.0")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("com.google.android.gms:play-services-location:18.0.0")
    compileOnly("io.realm.kotlin:library-base:0.7.0")
}

android {
    compileSdkVersion(31)
    defaultConfig {
        applicationId = "com.example.harmanweatherapp.android"
        minSdkVersion(21)
        targetSdkVersion(31)
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}