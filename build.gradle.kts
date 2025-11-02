plugins {
    kotlin("jvm") version "2.0.0"
    id("io.ktor.plugin") version "3.0.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0"
    application
}

application { mainClass.set("ApplicationKt") }

repositories { mavenCentral() }

dependencies {
    val ktor = "3.0.0"
    implementation("io.ktor:ktor-server-netty:$ktor")
    implementation("io.ktor:ktor-server-core:$ktor")
    implementation("io.ktor:ktor-server-call-logging:$ktor")
    implementation("io.ktor:ktor-server-cors:$ktor")
    implementation("io.ktor:ktor-server-status-pages:$ktor")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor")
    implementation("io.ktor:ktor-server-websockets:$ktor")
    implementation("io.ktor:ktor-server-swagger:$ktor")
    testImplementation(kotlin("test"))
}
