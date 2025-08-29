import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.tasks.JavaExec // Import the required class for JavaExec tasks
import org.gradle.jvm.toolchain.JavaToolchainService // Import the service to access toolchains

plugins {
    idea
    java
    id("gg.essential.loom") version "1.3+"
    id("dev.architectury.architectury-pack200") version "0.1.3"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    kotlin("jvm") version "1.9.0"
}

// Project constants
val baseGroup: String by project
val mcVersion: String by project
val version: String by project
val mixinGroup = "$baseGroup.mixin"
val modid: String by project

// Toolchains
java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(8))
}

// Get the Java Toolchain service to find the JDK path
val javaToolchains = project.extensions.getByType(JavaToolchainService::class.java)

sourceSets.main {
    output.setResourcesDir(sourceSets.main.flatMap { it.java.classesDirectory })
    java.srcDir(layout.projectDirectory.dir("src/main/kotlin"))
    kotlin.destinationDirectory.set(java.destinationDirectory)
}

// Dependencies
repositories {
    mavenCentral()
    mavenLocal()

    maven("https://repo.spongepowered.org/maven/")
    maven("https://repo.essential.gg/repository/maven-public/")

    // Optional auth bypass
    maven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1")

    maven("https://repo.nea.moe/releases")
    maven("https://maven.notenoughupdates.org/releases")
}

val shadowImpl: Configuration by configurations.creating {
    configurations.implementation.get().extendsFrom(this)
}
val shadowModImpl: Configuration by configurations.creating {
    configurations.modImplementation.get().extendsFrom(this)
}
val devenvMod: Configuration by configurations.creating {
    isTransitive = false
    isVisible = false
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.30")
    // Annotation processors
    annotationProcessor("com.google.code.gson:gson:2.2.4")
    annotationProcessor("com.google.guava:guava:17.0")
    annotationProcessor("org.spongepowered:mixin:0.8.5-SNAPSHOT")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    minecraft("com.mojang:minecraft:1.8.9")
    mappings("de.oceanlabs.mcp:mcp_stable:22-1.8.9")
    forge("net.minecraftforge:forge:1.8.9-11.15.1.2318-1.8.9")

    implementation(kotlin("stdlib-jdk8"))
    shadowImpl("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3") {
        exclude(group = "org.jetbrains.kotlin")
    }

    implementation("com.squareup.okhttp3:okhttp:5.1.0")

    // Mixins
    shadowImpl("org.spongepowered:mixin:0.7.11-SNAPSHOT") {
        isTransitive = false
    }

    // Optional: DevAuth
    runtimeOnly("me.djtheredstoner:DevAuth-forge-legacy:1.1.2")

    // Assuming libs.moulconfig and libs.libautoupdate are defined in your gradle/libs.versions.toml
    shadowModImpl(libs.moulconfig)
    devenvMod(variantOf(libs.moulconfig) { classifier("test") })
    shadowImpl(libs.libautoupdate)

    shadowImpl("org.jetbrains.kotlin:kotlin-reflect:1.9.0")
    shadowImpl("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3") {
        exclude(group = "org.jetbrains.kotlin")
        exclude(group = "com.google.code.gson")
    }
    shadowImpl("org.spongepowered:mixin:0.7.11-SNAPSHOT") {
        isTransitive = false
        exclude(group = "com.google.code.gson")
    }
    shadowImpl(libs.libautoupdate) {
        exclude(group = "com.google.code.gson")
    }
}

configurations.all {
    resolutionStrategy {
        force("com.google.code.gson:gson:2.2.4")
    }
}


// Loom config
loom {
    runs {
        named("client") {
            client()
            property("mixin.debug", "true")
            vmArgs("-Xmx4G", "-Xms4G", "-XX:MaxDirectMemorySize=512M", "-XX:MaxMetaspaceSize=256M", "-XX:ReservedCodeCacheSize=128M")
            programArgs("--tweakClass", "io.github.notenoughupdates.moulconfig.tweaker.DevelopmentResourceTweaker")
        }
        named("server") {
            server()
        }
    }
    forge {
        pack200Provider.set(dev.architectury.pack200.java.Pack200Adapter())
        mixinConfig("mixins.$modid.json")
    }
    @Suppress("UnstableApiUsage")
    mixin {
        defaultRefmapName.set("mixins.$modid.refmap.json")
    }
}

kotlin {
    sourceSets.all {
        languageSettings {
            languageVersion = "1.9" // safer than 2.0
            enableLanguageFeature("BreakContinueInInlineLambdas")
        }
    }
}

// Tasks
tasks.compileJava {
    dependsOn(tasks.processResources)
}

// This is the correct way to set the runtime JDK for runClient
tasks.withType<JavaExec>().configureEach {
    val launcher = javaToolchains.launcherFor(java.toolchain)
    javaLauncher.set(launcher)
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Jar> {
    archiveBaseName.set(modid)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest.attributes.run {
        this["FMLCorePluginContainsFMLMod"] = "true"
        this["ForceLoadAsMod"] = "true"
        this["TweakClass"] = "org.spongepowered.asm.launch.MixinTweaker"
        this["MixinConfigs"] = "mixins.$modid.json"
    }
}
tasks.processResources {
    inputs.property("version", project.version)
    inputs.property("mcversion", mcVersion)
    inputs.property("modid", modid)
    inputs.property("mixinGroup", mixinGroup)

    filesMatching(listOf("mcmod.info", "mixins.$modid.json")) {
        expand(inputs.properties)
    }
    rename("(.+_at.cfg)", "META-INF/$1")
}

// Remap Jar
val remapJar by tasks.named<net.fabricmc.loom.task.RemapJarTask>("remapJar") {
    archiveClassifier.set("")
    from(tasks.shadowJar)
    input.set(tasks.shadowJar.get().archiveFile)
}

tasks.shadowJar {
    destinationDirectory.set(layout.buildDirectory.dir("badjars"))
    archiveClassifier.set("all-dev")
    configurations = listOf(shadowImpl, shadowModImpl)
    doLast {
        configurations.forEach {
            println("Copying jars into mod: ${it.files}")
        }
    }
    exclude("META-INF/versions/**")

    relocate("io.github.moulberry.moulconfig", "$baseGroup.deps.moulconfig")
    relocate("moe.nea.libautoupdate", "$baseGroup.deps.libautoupdate")
}
tasks.jar {
    archiveClassifier.set("nodeps")
    destinationDirectory.set(layout.buildDirectory.dir("badjars"))
}
tasks.assemble.get().dependsOn(tasks.remapJar)

// Kotlin compile options
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}