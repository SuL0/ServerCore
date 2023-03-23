package kr.sul.servercore.util

import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import java.util.logging.Level

object KeepExceptionAlert {
    // when plugin got disabled, all scheduler related to that plugin also got destroyed too, so i added plugin as parameter
    fun alert(e: Exception?, msg: String, interval: Long, givenPlugin: Plugin, conditionToCancelAlert: (() -> Boolean)?=null) {
        e?.printStackTrace()
        object: BukkitRunnable() {
            override fun run() {
                if (conditionToCancelAlert?.invoke() == true) {
                    cancel()
                }
                Bukkit.getLogger().log(
                    Level.WARNING,
                    "\n$msg\n"
                )
            }
        }.runTaskTimer(givenPlugin, interval, interval)
    }
}