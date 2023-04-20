package kr.sul.servercore.util

import kr.sul.servercore.ServerCore.Companion.plugin
import org.bukkit.Bukkit

object UptimeBasedOnTick {
    var uptimeBasedOnTick = 0L

    init {
        Bukkit.getScheduler().runTaskTimer(plugin, { _ ->
            uptimeBasedOnTick += 1
        }, 0L, 1L)
    }
}