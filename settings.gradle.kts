pluginManagement {
    repositories {
        maven(url = "https://maven.aliyun.com/repository/public")
        maven(url = "https://mvn.0110.be/releases")
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven(url = "https://maven.aliyun.com/repository/public")
        maven(url = "https://mvn.0110.be/releases")
        google()
        mavenCentral()
    }
}

rootProject.name = "EchoStrings"
include(":app")
 