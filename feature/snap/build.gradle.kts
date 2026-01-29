plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.android.secrets)
}

android {
    namespace = "br.com.dev360.gemisnap.feature.snap"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    packaging {
        resources {
            excludes += "META-INF/*"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.activity.compose)

    implementation(libs.androidx.compose.ui.tooling.preview)

    implementation(project(":core:shared"))
    implementation(project(":core:shared-ui"))
    implementation(project(":core:networking"))


    debugImplementation(libs.androidx.compose.ui.test.manifest)
    androidTestImplementation(libs.koin.test)

    androidTestImplementation(libs.test.mockk.android)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
}