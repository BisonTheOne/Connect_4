plugins {
    application
    java
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "com.example"
version = "1.0"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

javafx {
    version = "21"
    modules = listOf("javafx.controls")
}

application {
    mainClass.set("com.bart.connect4.Main")
}