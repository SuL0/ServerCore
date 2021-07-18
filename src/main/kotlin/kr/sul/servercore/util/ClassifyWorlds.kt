package kr.sul.servercore.util

import kr.sul.servercore.ServerCore.Companion.plugin
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.world.WorldLoadEvent
import java.util.*

object ClassifyWorlds: Listener {
    val spawnWorlds = arrayListOf<World>()
    val normalWorlds = arrayListOf<World>()
    val hardTestWorlds = arrayListOf<World>()
    val hardWorlds = arrayListOf<World>()

    init {
        Bukkit.getScheduler().runTask(plugin) {
            Bukkit.getWorlds().forEach {
                registerWorld(it)
            }
        }
    }

    @EventHandler
    fun onWorldLoad(e: WorldLoadEvent) {
        registerWorld(e.world)
    }
    private fun registerWorld(world: World) {
        val worldName = world.name.uppercase(Locale.ENGLISH)
        when {
            worldName.startsWith("SPAWN-") -> spawnWorlds.add(world)
            worldName.startsWith("NORMAL-") -> normalWorlds.add(world)
            worldName.startsWith("HARD_TEST-") -> hardTestWorlds.add(world)
            worldName.startsWith("HARD-") -> hardWorlds.add(world)
        }
    }


    fun isSpawnWorld(world: World): Boolean {
        return spawnWorlds.contains(world)
    }
//    fun inGameWorld(world: World): Boolean {}
}