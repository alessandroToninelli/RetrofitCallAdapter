import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.Date
import kotlin.io.println
import java.util.Properties
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.4.31"
    id("com.jfrog.bintray") version "1.8.5"
    id("org.jetbrains.dokka") version "0.10.0"
    id("com.github.johnrengelman.shadow") version "6.1.0"
    `maven-publish`
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

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<ShadowJar>{
    archiveBaseName.set("retrofitcalladapter")
    archiveVersion.set("")
    archiveClassifier.set("")
}

//tasks {
//
//    register<Jar>("sourceJar") {
//        archiveClassifier.set("sources")
//        from(sourceSets.getByName("main").allSource)
//    }
//
//    dokka {
//        outputFormat = "html"
//        outputDirectory = "$buildDir/javadoc"
//    }
//
//    bintrayUpload {
//        dependsOn("build")
//        dependsOn("sourceJar")
//        dependsOn("generateMetadataFileForDefaultPublication")
//        dependsOn("generatePomFileForDefaultPublication")
//    }
//}

//val props =  Properties().also {
//    it.load(file("local.properties").inputStream())
//}
//fun getBintrayUser(): String = props.getProperty("bintrayUser")
//fun getBintrayApiKey(): String = props.getProperty("bintrayKey")


//publishing {
//    publications {
//        create<MavenPublication>("default") {
//            artifactId = project.name
//            groupId = project.group.toString()
//            version = project.version.toString()
//            from(components["java"])
//            artifact(tasks["sourceJar"])
//            pom {
//                name.set(rootProject.name)
//                url.set("https://github.com/alessandroToninelli/RetrofitCallAdapter")
//                licenses {
//                    license {
//                        name.set("The Apache Software License, Version 2.0")
//                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
//                    }
//                }
//                developers {
//                    developer {
//                        name.set("Alessandro Toninelli")
//                        id.set("alessandroToninelli")
//                    }
//                }
//                scm {
//                    url.set("https://github.com/alessandroToninelli/RetrofitCallAdapter")
//                }
//            }
//        }
//    }
//}

//bintray {
//    user = getBintrayUser()
//    key = getBintrayApiKey()
//    setPublications("default")
//    publish = true
//    pkg.apply {
//        repo = "toninelli-library"
//        name = rootProject.name
//        setLicenses("Apache-2.0")
//        vcsUrl = "https://github.com/alessandroToninelli/Kotlin-Retrofit-NetworkCallAdapter.git"
//        version.apply {
//            name = "${project.version}"
//            desc = "Retofit Call Adapter"
//            released = "${Date()}"
//            vcsTag = project.version.toString()
//        }
//    }
//}
