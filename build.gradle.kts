import com.github.jengelman.gradle.plugins.shadow.tasks.ConfigureShadowRelocation
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.5.21"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "kr.sul"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://repo.dmulloy2.net/nexus/repository/public/")
    maven("https://repo.codemc.io/repository/maven-snapshots")
    maven("https://maven.elmakers.com/repository/")
    maven("https://repo.citizensnpcs.co/")
    mavenLocal()
}

val pluginStorage = "C:/MC-Development/PluginStorage"
val nmsBukkitPath = "C:/MC-Development/마인즈서버/paper-1.12.2-R0.1-SNAPSHOT-shaded.jar"
val copyPluginDestination = "C:/MC-Development/마인즈서버/plugins"
dependencies {
    implementation(kotlin("stdlib-jdk8"))
    compileOnly(files(nmsBukkitPath))

    compileOnly("com.github.MilkBowl","VaultAPI", "1.7")
    compileOnly("net.luckperms", "api", "5.3")
    compileOnly("net.citizensnpcs", "citizensapi", "2.0.28-SNAPSHOT")
    compileOnly("com.comphenix.protocol", "ProtocolLib", "4.6.0")
    compileOnly("kr.sul", "MiscellaneousThings-2", "1.0-SNAPSHOT")
    compileOnly(files("$pluginStorage/Dependencies/item-nbt-api-plugin-2.6.0.jar"))
    compileOnly(files("$pluginStorage/ResourcepackSoundPlayer_S.jar"))
    compileOnly(files("$pluginStorage/CrackShotAddition_S.jar"))

    
    // SHADE
    implementation("net.wesjd", "anvilgui", "1.5.0-SNAPSHOT")
    implementation("net.lingala.zip4j", "zip4j", "2.9.0")
//    runtimeOnly("net.lingala.zip4j", "zip4j", "2.7.0")
    implementation("xyz.upperlevel.spigot.book", "spigot-book-api", "1.5")
    // PacketWrapper을 Project 만들어서 빌드하고 여기서 shade해서 쓸까?
}

// Gradle register<>() vs create<>()  https://stackoverflow.com/questions/53654190/what-is-the-difference-between-registering-and-creating-in-gradle-kotlin-dsl
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
        into(file(copyPluginDestination))
    }


//    // 자동으로 모든 Shade된 플러그인들 경로 변경
//    val relocateShadowJar = register<ConfigureShadowRelocation>("relocateShadowJar") {
//        target = shadowJar.get()
//        prefix = "shaded" // Default value is "shadow"
//    }

    // https://github.com/HeartPattern/SpringFox/commit/c20e48b5cebfda643342652964d9eb66ff0f498f
    // https://www.spigotmc.org/threads/how-to-shade-apis-with-gradle.416273/
    // 기본으로 등록한 ShadowJar Task. 추가적으로 다른 설정값을 가진 ShadowJar Task 를 만들고 싶다면, register<ShadowJar>("이름") { // 세팅값 조정할 곳 }
    shadowJar {
        archiveFileName.set("${project.name}_S.jar")
        destinationDirectory.set(file(pluginStorage))

        dependencies {
            // implementation 만 가능 (compileOnly 는 불가)
            include(dependency("net.wesjd:anvilgui"))
            include(dependency("net.lingala.zip4j:zip4j"))
            include(dependency("xyz.upperlevel.spigot.book:spigot-book-api"))
        }
        finalizedBy(copyPlugin_2)
    }

    build { dependsOn(shadowJar) }
}