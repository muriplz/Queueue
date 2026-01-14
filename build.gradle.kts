plugins {
    id("java")
}

group = "dev.muriplz"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    //implementation("org.spongepowered:mixin:0.8.5")
    compileOnly(files("libs/HytaleServer.jar"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}