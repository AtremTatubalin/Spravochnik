import org.gradle.api.tasks.testing.Test

plugins {
    id("spravochnik.android.library")
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "ru.constructor.handbook.core.database"
    defaultConfig { testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner" }
}

dependencies {
    implementation(project(":core:reference"))
    implementation(project(":core:projects"))
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.serialization.json)
    kapt(libs.androidx.room.compiler)

    testImplementation(libs.junit)
    testImplementation(libs.androidx.room.testing)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.robolectric)
    testRuntimeOnly(libs.robolectric.android)
}

kapt { arguments { arg("room.schemaLocation", "$projectDir/schemas") } }

tasks.withType<Test>().configureEach {
    listOf("http.proxyHost", "http.proxyPort", "https.proxyHost", "https.proxyPort").forEach { key ->
        System.getProperty(key)?.let { systemProperty(key, it) }
    }
}
