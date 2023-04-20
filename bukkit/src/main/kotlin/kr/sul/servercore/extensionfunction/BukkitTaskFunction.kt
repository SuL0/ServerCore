package kr.sul.servercore.extensionfunction

import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitTask

object BukkitTaskFunction {
    fun BukkitTask.cancelAfter(plugin: Plugin, tick: Long) {
        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            this.cancel()
        }, tick)
    }
}