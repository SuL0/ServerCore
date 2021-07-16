package kr.sul.servercore.util

import kr.sul.servercore.ServerCore.Companion.plugin
import org.bukkit.Bukkit
import org.bukkit.World

object ClassifyWorlds {
    // TODO use file to define world's type.
    val spawnWorlds = arrayListOf<World>()
    val inGameWorlds = arrayListOf<World>()

    init {
        Bukkit.getScheduler().runTask(plugin) {
            listOf("SPAWN-2").forEach {                       // 일단 테스트용. 나중에 월드 이름 SPAWN-01, 02 로 ㄱㄱ
                spawnWorlds.add(Bukkit.getWorld(it))
            }
        }
    }

    fun isSpawnWorld(world: World): Boolean {
        return (world.name.startsWith("SPAWN", true))
    }
//    fun inGameWorld(world: World): Boolean {}
}