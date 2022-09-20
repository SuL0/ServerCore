package kr.sul.servercore.util

import kr.sul.servercore.ServerCore.Companion.plugin
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.world.WorldLoadEvent
import org.bukkit.event.world.WorldUnloadEvent

object ClassifyWorlds: Listener {
    val spawnWorlds = WorldList("spawn")
    val beachTownWorlds = WorldList("BeachTown")

    fun isSpawnWorld(world: World): Boolean {
        return spawnWorlds.contains(world)
    }
    fun isBeachTownWorld(world: World): Boolean {
        return beachTownWorlds.contains(world)
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



    fun teleportToSpawn(p: Player) {
        // TODO 스폰 월드 랜덤
        // 아래는 임시 코드임
        if (Bukkit.getWorld("Spawn") != null) {
            p.teleport(Location(Bukkit.getWorld("Spawn"), 845.5, 56.0, 788.5))
        }
    }
}