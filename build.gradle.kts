import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.net.URI

plugins {
    id("fabric-loom") version "1.10-SNAPSHOT"
    id("maven-publish")
    id("org.jetbrains.kotlin.jvm") version "2.1.20"
    kotlin("plugin.serialization") version "1.8.0"
}

version = project.property("mod_version")!!
group = project.property("maven_group")!!

base {
    archivesName.set(project.property("archives_base_name") as String)
}

repositories {
    // Add repositories to retrieve artifacts from in here.
    // Only use this when depending on other mods, because Loom adds essential
    // repositories for Minecraft and libraries automatically.
    maven { url = URI("https://repo.hypixel.net/repository/Hypixel/") }
}

loom {
    splitEnvironmentSourceSets()

    mods {
        create("lyrablock") {
            // sourceSet(sourceSets.main)
            sourceSet(sourceSets["client"])
        }
    }
}

val ktor_version: String by project

dependencies {
    implementation("org.reflections:reflections:0.10.2")
    implementation("io.ktor:ktor-client-core:${ktor_version}")
    implementation("io.ktor:ktor-client-cio:${ktor_version}")
    implementation("net.hypixel:mod-api:1.0.1")
    minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")
    mappings("net.fabricmc:yarn:${project.property("yarn_mappings")}:v2")
    modImplementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")

    modImplementation("net.fabricmc.fabric-api:fabric-api:${project.property("fabric_version")}")
    modImplementation("net.fabricmc:fabric-language-kotlin:${project.property("fabric_kotlin_version")}")
}

tasks.processResources {
    inputs.property("version", project.version)

    filesMatching("fabric.mod.json") {
        expand(mapOf("version" to inputs.properties["version"]))
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(21)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.jar {
    inputs.property("archivesName", base.archivesName.get())

    from("LICENSE") {
        rename { "${it}_${inputs.properties["archivesName"]}" }
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = project.property("archives_base_name") as String
            from(components["java"])
        }
    }

    repositories {
        // Add repositories for publishing here
    }
}
