package kr.sul.servercore.util

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

    @EventHandler
    fun onWorldLoad(e: WorldLoadEvent) {
        val worldName = e.world.name.uppercase(Locale.ENGLISH)

        when {
            worldName.startsWith("SPAWN-") -> spawnWorlds.add(e.world)
            worldName.startsWith("NORMAL-") -> normalWorlds.add(e.world)
            worldName.startsWith("HARD_TEST-") -> hardTestWorlds.add(e.world)
            worldName.startsWith("HARD-") -> hardWorlds.add(e.world)
        }
    }

    fun isSpawnWorld(world: World): Boolean {
        return spawnWorlds.contains(world)
    }
//    fun inGameWorld(world: World): Boolean {}
}