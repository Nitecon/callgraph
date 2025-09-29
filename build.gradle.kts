plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.8.22"
    id("org.jetbrains.intellij") version "1.13.3"
}

group = "com.nitecon"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2023.1.5")
    type.set("RD") // Rider
    plugins.set(listOf("com.intellij.cidr.base"))
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    patchPluginXml {
        sinceBuild.set("231")
        untilBuild.set("241.*")
        
        pluginDescription.set("""
            Call Graph Plugin for IntelliJ Rider that analyzes C++ code to show inbound and outbound calls 
            for specific classes. Helps developers understand code dependencies and call relationships.
        """.trimIndent())
        
        changeNotes.set("""
            <b>1.0.0</b>
            <ul>
                <li>Initial release</li>
                <li>Call graph visualization for C++ classes</li>
                <li>Show inbound and outbound calls</li>
                <li>Context menu integration</li>
            </ul>
        """.trimIndent())
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}