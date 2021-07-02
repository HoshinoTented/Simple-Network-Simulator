plugins {
    kotlin("jvm") version "1.5.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

fun kotlinx(name: String, version: String? = null): String {
    return "org.jetbrains.kotlinx:kotlinx-$name${version?.let { ":$it" }.orEmpty()}"
}

repositories {
    maven("https://maven.aliyun.com/repository/public")
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlinx("coroutines-core", "1.5.0"))
}
