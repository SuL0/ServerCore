plugins {
    kotlin("jvm") version "1.5.21" apply true
    `maven-publish` apply true
    `java-library` apply true
}

group = "kr.sul"
version = "1.0-SNAPSHOT"

val versionMirror = version
val rootName = "ServerCore"
val pluginStorage = "C:/MC-Development/PluginStorage"
val bukkitCopyDestination = "C:/MC-Development/마인즈서버/plugins"
val bungeeCopyDestination = "C:/MC-Development/Waterfall/plugins"

val getPluginName: (Project) -> String = { givenProject ->
    "$rootName-${givenProject.name}_S-${givenProject.version}.jar"
}
subprojects {
    apply(plugin="org.jetbrains.kotlin.jvm")
    apply(plugin="org.gradle.maven-publish")
    ext {
        set("rootName", "Guild")
        set("version", versionMirror)
        set("pluginStorage", pluginStorage)
    }

    tasks {
        compileJava.get().options.encoding = "UTF-8"
        compileKotlin.get().kotlinOptions.jvmTarget = "1.8"
        compileTestKotlin.get().kotlinOptions.jvmTarget = "1.8"
    }
    publishing {
        java {
            withSourcesJar()
            withJavadocJar()
        }
        publications {
            create<MavenPublication>("maven") {
                groupId = rootProject.group as String
                version = rootProject.version as String
                from(components["java"])
            }
        }
    }



    tasks {
        // Gradle register<>() vs create<>()  https://stackoverflow.com/questions/53654190/what-is-the-difference-between-registering-and-creating-in-gradle-kotlin-dsl
        val fatJar = register<Jar>("fatJar") {
            duplicatesStrategy = DuplicatesStrategy.WARN
            val pluginName = getPluginName.invoke(project)
            archiveFileName.set(pluginName)
            val destiny = when (project.name) {
                "Bungee" -> bungeeCopyDestination
                "Bukkit" -> bukkitCopyDestination
                else -> throw Exception()
            }
            destinationDirectory.set(file(destiny))  // clean 영향 안 받음. (영향받는 건 project.buildDir으로 설정했을 때)
            from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })

            finalizedBy(publishToMavenLocal)
        }

        build { dependsOn(fatJar) }
    }
}