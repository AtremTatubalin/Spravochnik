plugins { `kotlin-dsl` }
group = "ru.constructor.handbook.buildlogic"
dependencies { implementation("com.android.tools.build:gradle:8.11.1"); implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.2.20"); implementation("org.jetbrains.kotlin:compose-compiler-gradle-plugin:2.2.20") }
gradlePlugin { plugins {
 register("androidLibrary") { id="spravochnik.android.library"; implementationClass="AndroidLibraryConventionPlugin" }
 register("androidFeature") { id="spravochnik.android.feature"; implementationClass="AndroidFeatureConventionPlugin" }
 register("kotlinLibrary") { id="spravochnik.kotlin.library"; implementationClass="KotlinLibraryConventionPlugin" }
} }
