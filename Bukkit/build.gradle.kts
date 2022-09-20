group = "kr.sul"
version = ext.get("version")!!

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://repo.dmulloy2.net/nexus/repository/public/")
    maven("https://repo.codemc.io/repository/maven-snapshots")
    maven("https://maven.elmakers.com/repository/")
    maven("https://repo.citizensnpcs.co/")
    maven("https://nexus-repo.jordanosterberg.com/repository/maven-releases/")
    maven("https://github.com/deanveloper/SkullCreator/raw/mvn-repo/")
    maven("https://repo.codemc.org/repository/maven-public/")
    maven("https://repo.aikar.co/content/groups/aikar/")
    maven("https://jitpack.io")
}

val pluginStorage = ext.get("pluginStorage")!!
dependencies {
    compileOnly(kotlin("stdlib-jdk8"))
    compileOnly("com.destroystokyo.paper:paper-api:1.12.2-R0.1-SNAPSHOT")
    compileOnly("com.destroystokyo.paper:paper:1.12.2-R0.1-SNAPSHOT")

    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("net.luckperms:api:5.3")
    compileOnly("net.citizensnpcs:citizensapi:2.0.29-SNAPSHOT")
    compileOnly("net.citizensnpcs:citizens-main:2.0.29-SNAPSHOT")
    // local에서 직접 install한 Citizen main을 가져오게
    compileOnly("com.comphenix.protocol:ProtocolLib:4.6.0")
    compileOnly("kr.sul:MiscellaneousThings-2:1.0-SNAPSHOT")
    compileOnly("de.tr7zw:item-nbt-api-plugin:2.6.0")
    compileOnly(files("$pluginStorage/ResourcepackSoundPlayer_S.jar"))
    compileOnly(files("$pluginStorage/CrackShotAddition_S.jar"))
//    api("co.aikar:acf-paper:0.5.1-SNAPSHOT")
//    api("redis.clients:jedis:4.2.3")
    val lampVersion = "3.0.8"
//    api("com.github.Revxrsal.Lamp:common:$lampVersion")
//    api("com.github.Revxrsal.Lamp:bukkit:$lampVersion")


    // api로 선언하면 transparent 때문에 ServerCore을 의존하는 프로젝트들은 api()로 된 것들도 dependency로 가져오게 됨
    implementation("net.wesjd:anvilgui:1.5.0-SNAPSHOT")
    implementation("net.lingala.zip4j:zip4j:2.10.0")
    implementation("xyz.upperlevel.spigot.book:spigot-book-api:1.5")
    implementation("dev.jcsoftware:JScoreboards:2.1.2-RELEASE")
    implementation("com.github.shynixn.mccoroutine:mccoroutine-bukkit-api:2.2.0")
    implementation("com.github.shynixn.mccoroutine:mccoroutine-bukkit-core:2.2.0")
    implementation("com.github.shynixn.mccoroutine:mccoroutine-bungeecord-api:2.2.0")
    implementation("com.github.shynixn.mccoroutine:mccoroutine-bungeecord-core:2.2.0")
    implementation("dev.dbassett:skullcreator:3.0.1")
    implementation("net.bytebuddy:byte-buddy:1.12.8")
    implementation("io.github.skytasul:guardianbeam:2.2.5")
    implementation("com.github.Revxrsal.Lamp:common:$lampVersion")
    implementation("com.github.Revxrsal.Lamp:bukkit:$lampVersion")
    implementation("redis.clients:jedis:4.2.3")
    implementation("org.apache.commons:commons-pool2:2.6.2")
    implementation("org.redisson:redisson:3.17.6")
    implementation("com.github.MilkBowl:VaultAPI:1.7")
    // PacketWrapper을 Project 만들어서 빌드하고 여기서 shade해서 쓸까?
}