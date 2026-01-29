plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "br.com.dev360.gemisnap.core.analytics"
    compileSdk = libs.versions.compileSdk.get().toInt()
}

dependencies {

}