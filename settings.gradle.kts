pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        // jcenter() // No es recomendable debido a la obsolescencia
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // jcenter() // No es recomendable debido a la obsolescencia
        maven { setUrl("https://jitpack.io") }
    }
}

rootProject.name = "Shop Market"
include(":app")
