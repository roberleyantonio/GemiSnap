plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "br.com.dev360.gemisnap.core.networking"
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
        buildConfig = true
    }

}

dependencies {
    implementation(libs.okhttp.logging)
    implementation(libs.okhttp)
    implementation(libs.httpcore)
    implementation(libs.okhttp.dns)

    api(libs.retrofit) {
        exclude(group = "com.squareup.okhttp3", module = "okhttp3")
    }

    api(libs.gson)
    implementation(libs.retrofit.converter)

    implementation(project(":core:shared"))
}