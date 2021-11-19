import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

val ktorVersion = "1.6.0"
val coroutineVersion = "1.4.3"

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    kotlin("plugin.serialization") version "1.5.31"
}

version = "1.0"

kotlin {
    android()

    val iosTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget = when {
        System.getenv("SDK_NAME")?.startsWith("iphoneos") == true -> ::iosArm64
//        System.getenv("NATIVE_ARCH")?.startsWith("arm") == true -> ::iosSimulatorArm64
        else -> ::iosX64
    }

    iosTarget("ios") {}

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "14.1"
        frameworkName = "MultiPlatformLibrary"
        podfile = project.file("../iosApp/Podfile")
    }
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                api("dev.icerock.moko:mvvm-core:0.11.0") // only ViewModel, EventsDispatcher, Dispatchers.UI
                api("dev.icerock.moko:mvvm-livedata:0.11.0") // api mvvm-core, LiveData and extensions
                api("dev.icerock.moko:mvvm-state:0.11.0") // api mvvm-livedata, ResourceState class and extensions
                api("dev.icerock.moko:resources:0.17.2")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
                implementation(
                    "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion-native-mt")
                implementation("io.ktor:ktor-client-core:$ktorVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting {
            dependencies {
                api("dev.icerock.moko:mvvm-livedata-material:0.11.0") // api mvvm-livedata, Material library android extensions
                api("dev.icerock.moko:mvvm-livedata-glide:0.11.0") // api mvvm-livedata, Glide library android extensions
                api("dev.icerock.moko:mvvm-livedata-swiperefresh:0.11.0") // api mvvm-livedata, SwipeRefreshLayout library android extensions
                api("dev.icerock.moko:mvvm-databinding:0.11.0") // api mvvm-livedata, DataBinding support for Android
                api("dev.icerock.moko:mvvm-viewbinding:0.11.0") // api mvvm-livedata, ViewBinding support for Android
                implementation(
                    "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion-native-mt")
                implementation("io.ktor:ktor-client-android:$ktorVersion")
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13.2")
            }
        }
        val iosMain by getting
        val iosTest by getting
    }

    kotlin {
        // export correct artifact to use all classes of library directly from Swift
        targets.withType(org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget::class.java).all {
            binaries.withType(org.jetbrains.kotlin.gradle.plugin.mpp.Framework::class.java).all {
                export("dev.icerock.moko:mvvm-core:0.11.0")
                export("dev.icerock.moko:mvvm-livedata:0.11.0")
                export("dev.icerock.moko:mvvm-state:0.11.0")
                export("dev.icerock.moko:resources:0.17.2")
            }
        }
    }
}

android {
    compileSdkVersion(31)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(31)
    }
}