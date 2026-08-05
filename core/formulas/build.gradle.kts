plugins { id("spravochnik.kotlin.library") }
dependencies { api(project(":core:formula-api")) }
dependencies { testImplementation(libs.junit) }
