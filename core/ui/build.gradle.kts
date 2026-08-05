plugins { id("spravochnik.android.library"); alias(libs.plugins.kotlin.compose) }
android { namespace="ru.constructor.handbook.core.ui"; buildFeatures.compose=true }
dependencies { implementation(platform(libs.androidx.compose.bom)); api(libs.androidx.compose.ui); api(libs.androidx.compose.material3); debugImplementation(libs.androidx.compose.ui.tooling) }
dependencies { implementation(project(":core:reference")) }
