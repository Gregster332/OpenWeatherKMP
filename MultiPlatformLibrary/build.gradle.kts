
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

val ktorVersion = "1.6.0"
val coroutineVersion = "1.4.3"

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    kotlin("plugin.serialization") version "1.5.31"
    id("io.realm.kotlin") version "0.7.0"
    jacoco
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
                api("dev.icerock.moko:geo:0.4.0")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
                implementation(
                    "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion-native-mt")
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.realm.kotlin:library-base:0.7.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation("io.mockk:mockk-common:1.12.1")

                //implementation("io.mockative:mockative:1.1.2")
                //implementation("io.mockative:mockative-processor:1.1.2")
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
                //implementation("org.junit.jupiter:junit-jupiter:5.8.2")
                implementation("io.mockk:mockk:1.12.1")
//                implementation("junit:junit:4.13.2")

            }
        }
        val iosMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-ios:$ktorVersion")
            }
        }
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



//android {
//    unitTestVariants.all {
//        if (it.name == "testDebugUnitTest") {
//            extensions.configure(kotlinx.kover.api.KoverTaskExtension::class) {
//                isEnabled = true
//                binaryReportFile.set(file("$buildDir/custom/debug-report.bin"))
//                includes = listOf("com.example.*")
//                excludes = listOf("com.example.subpackage.*")
//            }
//        }
//    }
//}
jacoco {
    toolVersion = "0.8.6"
}

val jacocoTestReport by tasks.creating(JacocoReport::class.java) {
    reports {
        xml.isEnabled = true
        csv.isEnabled = false
        html.isEnabled = true
    }
}

tasks.withType<Test> {
    finalizedBy(jacocoTestReport)
}

//tasks {
//    named("allTests") {
//        finalizedBy(jacocoTestReport)
//    }
//    withType<JacocoReport> {
//        dependsOn("allTests")
//
//        classDirectories.from(buildDir.resolve("classes/kotlin/jvm").canonicalFile.walkBottomUp().toSet())
//        sourceDirectories.from("commonMain/src", "androidMain/src")
//
//        executionData.setFrom(buildDir.resolve("jacoco/testDebugUnitTest.exec"))
//        reports {
//            xml.required.set(true)
//            html.required.set(true)
//        }
//    }
//}

val jacocoTestCoverageVerification by tasks.creating(JacocoCoverageVerification::class.java) {
    dependsOn(jacocoTestReport)
    violationRules {
        rule {
            limit {
                minimum = "0.8".toBigDecimal()
            }
        }
    }
}

val testCoverage by tasks.registering {
    group = "verification"
    description = "Runs the unit tests with coverage."

    dependsOn("test", jacocoTestReport, jacocoTestCoverageVerification)
    tasks["jacocoTestReport"].mustRunAfter("test")
    tasks["jacocoTestCoverageVerification"].mustRunAfter("jacocoTestReport")
}