plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.paparazzi)
}

android {
    namespace = "br.com.dev360.gemisnap.core.sharedui"
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
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.compose.ui)
    api(libs.androidx.compose.ui.graphics)
    api(libs.androidx.compose.material3)
    api(libs.androidx.appcompat)
    api(libs.compose.coil)

    api(libs.splash.screen)
    api(libs.compose.fonts)
    api(libs.androidx.compose.material.icons.extended)

    api(libs.multiplatform.markdown.renderer)
    api(libs.multiplatform.markdown.renderer.m3)

    implementation(libs.androidx.compose.ui.tooling.preview)

    implementation(project(":core:shared"))
}