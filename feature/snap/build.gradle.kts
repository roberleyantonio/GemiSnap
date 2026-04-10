plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.android.secrets)
    alias(libs.plugins.paparazzi)
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

kotlin {
    jvmToolchain(17)
}

tasks.withType<Test>().configureEach {
    javaLauncher.set(
        javaToolchains.launcherFor {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    )
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

    testImplementation(libs.paparazzi)
}