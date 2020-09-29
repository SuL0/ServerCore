plugins {
    kotlin("jvm") version "1.4.10"
}

group = "kr.sul"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://repo.dmulloy2.net/nexus/repository/public/")
    mavenLocal()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    compileOnly("com.destroystokyo.paper", "paper-api", "1.12.2-R0.1-SNAPSHOT")
    compileOnly("org.spigotmc", "spigot", "1.12.2-R0.1-SNAPSHOT")
    compileOnly("com.comphenix.protocol", "ProtocolLib", "4.5.1")
    compileOnly(files("C:/Users/PHR/Desktop/PluginStorage/NotFat/CrackShotAddition_S-NotFat.jar"))
    compileOnly(files("C:/Users/PHR/Desktop/PluginStorage/Dependencies/item-nbt-api-plugin-2.5.0.jar"))
}


tasks.compileJava.get().options.encoding = "UTF-8"

tasks {
    compileKotlin.get().kotlinOptions.jvmTarget = "1.8"
    compileTestKotlin.get().kotlinOptions.jvmTarget = "1.8"

    jar {
        archiveFileName.set("${project.name}_S.jar")
        destinationDirectory.set(file("C:/Users/PHR/Desktop/PluginStorage"))
    }
}
