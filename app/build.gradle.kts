plugins { alias(libs.plugins.android.application); alias(libs.plugins.kotlin.android); alias(libs.plugins.kotlin.compose) }
android { namespace="ru.constructor.handbook"; compileSdk=36
 defaultConfig { applicationId="ru.constructor.handbook"; minSdk=26; targetSdk=36; versionCode=1; versionName="0.1.0" }
 buildFeatures.compose=true
 compileOptions { sourceCompatibility=JavaVersion.VERSION_17; targetCompatibility=JavaVersion.VERSION_17 }
}
kotlin { compilerOptions { jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17) } }
dependencies { implementation(project(":core:ui")); implementation(platform(libs.androidx.compose.bom)); implementation(libs.androidx.compose.ui); implementation(libs.androidx.compose.material3); implementation(libs.androidx.activity.compose); implementation(libs.androidx.navigation.compose); implementation(libs.androidx.core.ktx); implementation(libs.androidx.lifecycle.runtime)
 implementation(project(":feature:home"))
 implementation(project(":feature:search"))
 implementation(project(":feature:projects"))
 implementation(project(":feature:materials"))
 implementation(project(":feature:bearings"))
 implementation(project(":feature:fits"))
 implementation(project(":feature:threads"))
 implementation(project(":feature:fasteners"))
 implementation(project(":feature:profiles"))
 implementation(project(":feature:calculators"))
 implementation(project(":feature:reverse"))
 implementation(project(":feature:reports"))
 implementation(project(":feature:settings"))
 debugImplementation(libs.androidx.compose.ui.tooling)
}
