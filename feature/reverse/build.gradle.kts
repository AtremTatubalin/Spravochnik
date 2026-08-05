plugins { id("spravochnik.android.feature") }
android { namespace="ru.constructor.handbook.feature.reverse" }

dependencies { implementation(project(":core:reverse")); testImplementation(libs.junit) }
