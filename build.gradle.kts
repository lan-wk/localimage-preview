plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.25"
    id("org.jetbrains.intellij.platform") version "2.3.0"
}

group = "com.lwk"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin.html
dependencies {
    intellijPlatform {
        //create("IC", "2024.2.5")
        //androidStudio("2022.2.1.1", useInstaller = false)
        local(File("D:\\AS"))
        testFramework(org.jetbrains.intellij.platform.gradle.TestFrameworkType.Platform)
        // 新增 Dart 插件依赖
        plugin("Dart:243.23654.44")
    }
}

intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            sinceBuild = "AI-222"
        }

        changeNotes = """
      Initial version
    """.trimIndent()

        // 设置 Android Studio 路径
        //localPath = file("/path/to/your/android-studio")
    }
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "21"
        targetCompatibility = "21"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "21"
    }
}
