plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.secrets) apply false
    alias(libs.plugins.paparazzi) apply false
}

subprojects {
    afterEvaluate {
        val snapshotsDir = file("src/test/snapshots")

        tasks.matching { it.name == "verifyPaparazziDebug" }.configureEach {
            onlyIf { snapshotsDir.exists() }
        }
    }
}

gradle.projectsEvaluated {
    tasks.register("verifyAllPaparazzi") {
        group = "verification"
        dependsOn(
            subprojects.mapNotNull {
                it.tasks.findByName("verifyPaparazziDebug")?.path
            }
        )
    }

    tasks.register("recordAllPaparazzi") {
        group = "verification"
        dependsOn(
            subprojects.mapNotNull {
                it.tasks.findByName("recordPaparazziDebug")?.path
            }
        )
    }
}