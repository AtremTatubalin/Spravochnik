import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
class AndroidFeatureConventionPlugin : Plugin<Project> { override fun apply(target: Project)=with(target) {
 pluginManager.apply("spravochnik.android.library"); pluginManager.apply("org.jetbrains.kotlin.plugin.compose")
 extensions.configure<LibraryExtension> { buildFeatures.compose=true }
 dependencies { add("implementation", platform("androidx.compose:compose-bom:2025.10.01")); add("implementation", "androidx.compose.ui:ui"); add("implementation", "androidx.compose.material3:material3"); add("implementation", project(":core:ui")) }
} }
