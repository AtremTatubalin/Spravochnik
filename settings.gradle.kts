pluginManagement { repositories { google(); mavenCentral(); gradlePluginPortal() } }
dependencyResolutionManagement { repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS); repositories { google(); mavenCentral() } }
rootProject.name = "Spravochnik"
includeBuild("build-logic")
include(":app", ":core:common", ":core:ui", ":core:database", ":core:formula-api", ":core:formulas", ":core:validation", ":core:search", ":core:reference", ":core:projects", ":core:report", ":feature:home", ":feature:search", ":feature:projects", ":feature:materials", ":feature:bearings", ":feature:fits", ":feature:threads", ":feature:fasteners", ":feature:profiles", ":feature:calculators", ":feature:reverse", ":feature:reports", ":feature:settings")
