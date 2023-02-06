group = "kr.sul.servercore"
version = ext.get("version")!!


val rootName = ext.get("rootName")!! as String
bungee {
    name = rootName
}


repositories {
    mavenLocal()
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://jitpack.io")
}

dependencies {
    compileOnly(kotlin("stdlib-jdk8"))
    compileOnly("io.github.waterfallmc:waterfall-api:1.18-R0.1-SNAPSHOT")
    implementation("org.redisson:redisson:3.17.6")
    implementation("io.github.revxrsal:bungee:3.0.8")
    implementation("io.github.revxrsal:common:3.0.8")
    implementation("com.zaxxer:HikariCP:5.0.1")
}