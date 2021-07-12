plugins {
    kotlin("jvm") version "1.5.20"
}

group = "kr.sul"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://repo.dmulloy2.net/nexus/repository/public/")
    maven("https://repo.codemc.io/repository/maven-snapshots")
    mavenLocal()
}

val pluginStorage = "C:/Users/PHR/Desktop/PluginStorage"
dependencies {
    implementation(kotlin("stdlib-jdk8"))
    compileOnly("com.destroystokyo.paper", "paper-api", "1.12.2-R0.1-SNAPSHOT")
    compileOnly("org.spigotmc", "spigot", "1.12.2-R0.1-SNAPSHOT")

    compileOnly("com.comphenix.protocol", "ProtocolLib", "4.5.1")
    compileOnly("net.wesjd", "anvilgui", "1.5.0-SNAPSHOT")
    compileOnly("net.lingala.zip4j", "zip4j", "2.7.0")
    runtimeOnly("net.lingala.zip4j", "zip4j", "2.7.0")
    compileOnly("net.luckperms", "api", "5.3")

    compileOnly(files("$pluginStorage/CrackShotAddition_S.jar"))
    compileOnly(files("$pluginStorage/Dependencies/item-nbt-api-plugin-2.6.0.jar"))
}

val shade = configurations.create("shade")
shade.extendsFrom(configurations.compileOnly.get())

tasks {
    compileJava.get().options.encoding = "UTF-8"
    compileKotlin.get().kotlinOptions.jvmTarget = "1.8"
    compileTestKotlin.get().kotlinOptions.jvmTarget = "1.8"

    val copyPlugin = register<Copy>("copyPlugin") {
        from(files("$pluginStorage/${project.name}_S.jar"))
        into(file("C:/Users/PHR/Desktop/SERVER2/plugins"))
    }
    val copyPlugin_2 = register<Copy>("copyPlugin_2") {
        from(files("$pluginStorage/${project.name}_S.jar"))
        into(file("C:/Users/PHR/Desktop/마인즈서버/plugins"))
    }

    jar {
        archiveFileName.set("${project.name}_S.jar")
        destinationDirectory.set(file(pluginStorage))

        from(
            shade.filter { it.name.startsWith("anvilgui") || it.name.startsWith("zip4j") }
                .map {
                    if (it.isDirectory)
                        it
                    else
                        zipTree(it)
                }
        )
        finalizedBy(copyPlugin, copyPlugin_2)
    }
}
