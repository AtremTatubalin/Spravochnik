import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

public class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        pluginManager.apply("spravochnik.android.library")
        pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

        extensions.configure<LibraryExtension> {
            buildFeatures.compose = true
        }

        val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
        dependencies {
            add("implementation", platform(libs.findLibrary("androidx-compose-bom").get()))
            add("implementation", libs.findLibrary("androidx-compose-ui").get())
            add("implementation", libs.findLibrary("androidx-compose-material3").get())
            add("implementation", libs.findLibrary("androidx-activity-compose").get())
            add("implementation", libs.findLibrary("androidx-lifecycle-viewmodel-compose").get())
            add("testImplementation", libs.findLibrary("junit").get())
            add("testImplementation", libs.findLibrary("kotlinx-coroutines-core").get())
            add("implementation", project(":core:ui"))
        }
    }
}
