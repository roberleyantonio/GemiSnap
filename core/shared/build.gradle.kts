plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "br.com.dev360.gemisnap.core.shared"
    compileSdk = libs.versions.compileSdk.get().toInt()

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
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    api(libs.androidx.lifecycle.runtime.ktx)
    api(libs.androidx.core.ktx)

    api (platform(libs.koin.bom))
    api (libs.koin.core)
    api (libs.koin.android)
    api (libs.koin.compose)

    api(libs.koin.test)
    api(libs.test.mockk)
    api(libs.test.coroutines)
    api(libs.test.compose)
    api(libs.turbine)
}