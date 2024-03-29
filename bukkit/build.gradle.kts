group = "kr.sul.servercore"
version = ext.get("version")!!

repositories {
    mavenLocal()  // mavenLocal을 아래에 배치하면 빌드 때 마다 원격에서 의존성을 다운로드하게 됨 > 빌드타임 증가
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://repo.dmulloy2.net/nexus/repository/public/")
    maven("https://repo.codemc.io/repository/maven-snapshots")
    maven("https://maven.elmakers.com/repository/")
    maven("https://repo.citizensnpcs.co/")
//    maven("https://nexus-repo.jordanosterberg.com/repository/maven-releases/")
    maven("https://github.com/deanveloper/SkullCreator/raw/mvn-repo/")
    maven("https://repo.codemc.org/repository/maven-public/")
    maven("https://repo.aikar.co/content/groups/aikar/")
    maven("https://jitpack.io")
    maven("https://repo.purpurmc.org/snapshots")
    maven("https://repo.codemc.org/repository/maven-public/")
}

dependencies {
    compileOnly(kotlin("stdlib"))
    compileOnly("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")

    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("net.luckperms:api:5.4")
    compileOnly("net.citizensnpcs:citizensapi:2.0.29-SNAPSHOT")
    compileOnly("net.citizensnpcs:citizens-main:2.0.29-SNAPSHOT")
    // local에서 직접 install한 Citizen main을 가져오게
    compileOnly("com.comphenix.protocol:ProtocolLib:4.6.0")
//    compileOnly("kr.sul:MiscellaneousThings-2:1.0-SNAPSHOT")
    compileOnly("de.tr7zw:item-nbt-api-plugin:2.6.0")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
//    compileOnly("goldbigdragon.github.io:Minecraft-Resourcepack-Sound-Player:1.2") 1.12버전 전용이니


    // api로 선언하면 transparent 때문에 ServerCore을 의존하는 프로젝트들은 api()로 된 것들도 dependency로 가져오게 됨
    // implementation = compileOnly + runtimeOnly
    // shade 하려면 runtimeOnly 필요
    implementation("net.wesjd:anvilgui:1.5.0-SNAPSHOT")
    implementation("net.lingala.zip4j:zip4j:2.11.4")
    implementation("xyz.upperlevel.spigot.book:spigot-book-api:1.6")
//    implementation("dev.jcsoftware:JScoreboards:2.1.5-RELEASE")  // maven 저장소에서 내린 듯
    implementation("com.github.shynixn.mccoroutine:mccoroutine-bukkit-api:2.11.0")
    implementation("com.github.shynixn.mccoroutine:mccoroutine-bukkit-core:2.11.0")
    implementation("dev.dbassett:skullcreator:3.0.1")
//    implementation("net.bytebuddy:byte-buddy:1.14.2")
    implementation("io.github.skytasul:guardianbeam:2.3.1")
    implementation("com.github.Revxrsal.Lamp:common:3.1.5")
    implementation("com.github.Revxrsal.Lamp:bukkit:3.1.5")
    implementation("redis.clients:jedis:4.2.3")
    implementation("org.apache.commons:commons-pool2:2.11.1")
    implementation("org.redisson:redisson:3.20.0")
    implementation("org.apache.poi:poi:5.2.3")
    implementation("org.apache.poi:poi-ooxml:5.2.3")
    implementation("dev.jorel:commandapi-shade:8.8.0")
    // PacketWrapper을 Project 만들어서 빌드하고 여기서 shade해서 쓸까?
}