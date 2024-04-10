import java.io.File
import java.io.FileInputStream
import java.util.*

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    kotlin("kapt")
    alias(libs.plugins.com.google.dagger.hilt.android)
}

val prop = Properties().apply {
    load(FileInputStream(File(rootProject.rootDir, "ExampleSecretKeys.properties")))
}
val baseUrl: String = prop.getProperty("BASE_URL_API")
println("Property: $baseUrl")

android {
    namespace = BuildVersion.environment.applicationId
    compileSdk = BuildVersion.environment.compileSdkVersion

    defaultConfig {
        applicationId = BuildVersion.environment.applicationId
        minSdk = BuildVersion.environment.minSdkVersion
        targetSdk = BuildVersion.environment.targetSdkVersion
        versionCode = BuildVersion.environment.appVersionCode
        versionName = BuildVersion.environment.appVersionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildFeatures {
            buildConfig = true
        }

        buildConfigField("String", "BASE_URL", baseUrl)
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

    implementation(libs.bundles.layer.ui)
    implementation(platform(libs.androidx.compose.bom))


    kapt(libs.bundles.compilers.kapt.hilt)
    kapt(libs.bundles.compilers.kapt.room)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}