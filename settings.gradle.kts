pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.essential.gg/repository/maven-public/")
        maven("https://maven.architectury.dev/")
        maven("https://maven.fabricmc.net/")
        maven("https://maven.minecraftforge.net/")
    }
    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "gg.essential.loom" -> useModule("gg.essential:architectury-loom:${requested.version}")
            }
        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.6.0"
}
