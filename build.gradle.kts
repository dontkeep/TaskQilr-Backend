
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    kotlin("plugin.serialization") version "2.1.0"
}

group = "com.al"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.logback.classic)
    implementation(libs.ktor.client.okhttp)
    implementation("io.ktor:ktor-server-content-negotiation:3.0.3")
    implementation("io.ktor:ktor-serialization-gson:3.0.3")
    implementation("io.ktor:ktor-server-status-pages:3.0.3")
    implementation("io.ktor:ktor-server-cors:3.0.3")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.0.3")
    implementation("io.ktor:ktor-server-auth:3.0.3")
    implementation("io.ktor:ktor-server-sessions:3.0.3")

    implementation("io.ktor:ktor-client-core:3.0.3")
    implementation("io.ktor:ktor-client-cio:3.0.3")
    implementation("io.ktor:ktor-client-content-negotiation:3.0.3")

    implementation("io.ktor:ktor-client-logging:1.5.6")
    implementation("io.ktor:ktor-client-auth:3.0.3")
    implementation("io.ktor:ktor-client-json:3.0.3")
    implementation("io.ktor:ktor-client-serialization:3.0.3")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    implementation(libs.ktor.server.config.yaml)
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)
}
