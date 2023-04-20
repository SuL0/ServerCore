package kr.sul.servercore.util

import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.Plugin

/**
 * 일반 HashMap 이랑 동일하지만, onQuit onEntityDeath 시 해당 엔티티 값 삭제 기능 기본 탑재
 */
class EntityTempDataMap<V> private constructor(): HashMap<Entity, V>(), Listener {
    companion object {
        fun<V> create(plugin: Plugin): EntityTempDataMap<V> {
            val obj = EntityTempDataMap<V>()
            Bukkit.getPluginManager().registerEvents(obj, plugin)
            return obj
        }
    }

    @EventHandler
    fun onQuit(e: PlayerQuitEvent) {
        if (this.contains(e.player)) {
            this.remove(e.player)
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun onEntityDeath(e: EntityDeathEvent) {
        if (e.entity is Player) return
        if (this.contains(e.entity)) {
            this.remove(e.entity)
        }
    }
}