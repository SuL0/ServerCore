plugins {
    kotlin("jvm") version "1.8.0" apply true
//    id("kr.entree.spigradle") version "2.2.3" apply false
    id("kr.entree.spigradle.bungee") version "2.2.3" apply false
    `maven-publish` apply true
    `java-library` apply true
    id("io.papermc.paperweight.userdev") version "1.5.3" apply false
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenLocal()
    mavenCentral()
}
group = "kr.sul.servercore"
version = "1.19.4"

val versionMirror = version
val rootName = "ServerCore"
val bukkitCopyDestination = "C:/MC-Development/Minetopia/plugins"
val bungeeCopyDestination = "C:/MC-Development/Waterfall/plugins"

val getPluginName: (Project) -> String = { givenProject ->
    "$rootName-${givenProject.name}-${givenProject.version}.jar"
}
subprojects {
    apply(plugin="org.jetbrains.kotlin.jvm")
    apply(plugin="org.gradle.maven-publish")
    ext {
        set("rootName", rootName)
        set("version", versionMirror)
    }

    tasks {
        compileJava.get().options.encoding = "UTF-8"
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
        // val fatJar = register<Jar>("fatJar") 로 task를 만들면 원래의 jar task에서 하는 빌드는 하지 않고 오로지 shade만 하게 됨 -> jar task를 상속받는 방법은 없나?

        jar {
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE  // plugin.yml 을 SkullCreator 이 덮어쓰려 함 (INCLUDE 하면 덮어씀)
            archiveFileName.set(getPluginName.invoke(project))
            val destination = when (project.name) {
                "bungee" -> bungeeCopyDestination
                "bukkit" -> bukkitCopyDestination
                else -> throw Exception()
            }
            destinationDirectory.set(file(destination))  // clean 영향 안 받음. (영향받는 건 project.buildDir으로 설정했을 때)
            from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
            finalizedBy(publishToMavenLocal)
        }
//        build { dependsOn(jar) }
    }
}

project(":bungee").run {
    apply(plugin="kr.entree.spigradle.bungee")
}
project(":bukkit").run {
    apply(plugin="io.papermc.paperweight.userdev")
    dependencies {
        val paperweight = (this as ExtensionAware).extensions.getByName("paperweight")
                as io.papermc.paperweight.userdev.PaperweightUserDependenciesExtension
        paperweight.paperDevBundle("1.19.4-R0.1-SNAPSHOT")
    }
}