@Suppress("ktlint:standard:property-naming")
val kotlin_version: String by project

@Suppress("ktlint:standard:property-naming")
val logback_version: String by project

plugins {
    id("io.ktor.plugin") version "2.3.12"
    kotlin("jvm") version "2.0.20"
    kotlin("plugin.serialization") version "2.0.20"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")
    implementation(libs.h2.v2)
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    runtimeOnly(libs.slf4j.simple)
    implementation("org.postgresql:postgresql:42.7.2")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation("io.ktor:ktor-server-auth")
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-thymeleaf-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}

val exposedVersion: String by project
dependencies {
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-json:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-money:$exposedVersion")
    implementation("org.postgresql:postgresql:42.7.2") // Зависимость на PostgreSQL драйвер
}

dependencies {
    implementation("com.sksamuel.hoplite:hoplite-core:2.7.5")
    implementation("com.sksamuel.hoplite:hoplite-yaml:2.7.5")
    implementation("com.itextpdf:itext7-core:7.2.1")
    implementation("com.itextpdf:html2pdf:2.1.4")
}
