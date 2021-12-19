package kr.sul.servercore.util

import kr.sul.servercore.ServerCore.Companion.plugin
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.world.WorldLoadEvent
import org.bukkit.event.world.WorldUnloadEvent
import java.util.*

object ClassifyWorlds: Listener {
    val spawnWorlds = WorldList("spawn")

    init {
        Bukkit.getScheduler().runTask(plugin) {
            Bukkit.broadcastMessage("[spawnWorlds]")
            Bukkit.broadcastMessage("[spawnWorlds]: $spawnWorlds -${spawnWorlds.size}")
        }
    }

    fun isSpawnWorld(world: World): Boolean {
        return spawnWorlds.contains(world)
    }


    // 자동으로 월드 리스트 관리
    class WorldList(private val startWithThisName: String): ArrayList<World>(), Listener {
        init {
            Bukkit.getPluginManager().registerEvents(this, plugin)
        }

        @EventHandler
        fun onWorldLoad(e: WorldLoadEvent) {
            if (e.world.name.lowercase().startsWith(startWithThisName.lowercase())) {
                this.add(e.world)
            }
        }

        @EventHandler
        fun onWorldUnload(e: WorldUnloadEvent) {
            if (e.world.name.lowercase().startsWith(startWithThisName.lowercase())) {
                this.remove(e.world)
            }
        }
    }
}