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
    maven("http://maven.elmakers.com/repository/")
    mavenLocal()
}

val pluginStorage = "C:/Users/PHR/Desktop/PluginStorage"
dependencies {
    implementation(kotlin("stdlib-jdk8"))
    compileOnly("com.destroystokyo.paper", "paper-api", "1.12.2-R0.1-SNAPSHOT")
    compileOnly("org.spigotmc", "spigot", "1.12.2-R0.1-SNAPSHOT")

    compileOnly(files("$pluginStorage/CrackShotAddition_S.jar"))
    compileOnly(files("$pluginStorage/Dependencies/item-nbt-api-plugin-2.6.0.jar"))
    
    
    // SHADE
    compileOnly("net.wesjd", "anvilgui", "1.5.0-SNAPSHOT")
    compileOnly("net.lingala.zip4j", "zip4j", "2.7.0")
//    runtimeOnly("net.lingala.zip4j", "zip4j", "2.7.0")
    compileOnly("xyz.upperlevel.spigot.book", "spigot-book-api", "1.5")
    compileOnly("com.github.MilkBowl","VaultAPI", "1.7")
    compileOnly("com.elmakers.mine.bukkit","EffectLib", "9.0")
    compileOnly("com.comphenix.protocol", "ProtocolLib", "4.6.0")
    compileOnly("net.luckperms", "api", "5.3")
    compileOnly("net.citizensnpcs", "citizensapi", "2.0.28-SNAPSHOT")
    compileOnly(files("$pluginStorage/ResourcepackSoundPlayer_S.jar"))
}

val shade = configurations.create("shade")
shade.extendsFrom(configurations.compileOnly.get())

tasks {
    compileJava.get().options.encoding = "UTF-8"
    compileKotlin.get().kotlinOptions.jvmTarget = "1.8"
    compileTestKotlin.get().kotlinOptions.jvmTarget = "1.8"

//    val copyPlugin = register<Copy>("copyPlugin") {
//        from(files("$pluginStorage/${project.name}_S.jar"))
//        into(file("C:/Users/PHR/Desktop/SERVER2/plugins"))
//    }
    val copyPlugin_2 = register<Copy>("copyPlugin_2") {
        from(files("$pluginStorage/${project.name}_S.jar"))
        into(file("C:/Users/PHR/Desktop/마인즈서버/plugins"))
    }

    jar {
        archiveFileName.set("${project.name}_S.jar")
        destinationDirectory.set(file(pluginStorage))

        from(
            shade.filter { it.name.startsWith("anvilgui")
                    || it.name.startsWith("zip4j")
                    || it.name.startsWith("spigot-book-api")
                    || it.name.startsWith("VaultAPI")
                    || it.name.startsWith("EffectLib")
                    || it.name.startsWith("ResourcepackSoundPlayer")
                    || it.name.startsWith("luckperms")
                    || it.name.startsWith("citizensapi")
                    || it.name.startsWith("ProtocolLib") }
                .map {
                    if (it.isDirectory)
                        it
                    else
                        zipTree(it)
                }
        )
        finalizedBy(copyPlugin_2)
    }
}
