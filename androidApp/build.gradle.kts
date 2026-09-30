plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    androidTarget()
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(project(":shared"))
                implementation(libs.androidx.activity.compose)
                implementation(libs.compose.ui)
                implementation(libs.compose.material3)
                implementation(libs.androidx.lifecycle.viewmodel.compose)
            }
        }
    }
}

android {
    namespace = "com.example.notes.android"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.notes.android"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
