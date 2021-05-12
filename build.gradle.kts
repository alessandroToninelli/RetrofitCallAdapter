import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.Date
import kotlin.io.println
import java.util.Properties
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.5.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.4.32"
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "com.alessandrotoninelli"
version = "1.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.google.code.gson:gson:2.8.6")
    testImplementation(kotlin("test-junit"))
}

tasks.test {
    useJUnit()
}

tasks.withType<Jar>{
    archiveBaseName.set("retrofit-call-adapter")
    archiveVersion.set("")
    archiveClassifier.set("")
}


tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

tasks {


}
