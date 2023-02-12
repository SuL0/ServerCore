package kr.sul.servercore.util

import kr.sul.servercore.ServerCore.Companion.plugin
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import java.util.logging.Level

object KeepExceptionAlert {
    fun alert(e: Exception?, msg: String, conditionToCancelAlert: () -> Boolean, interval: Long) {
        e?.printStackTrace()
        object: BukkitRunnable() {
            override fun run() {
                if (conditionToCancelAlert.invoke()) {
                    cancel()
                }
                Bukkit.getLogger().log(
                    Level.WARNING,
                    "$msg\n\n"
                )
            }
        }.runTaskTimer(plugin, interval, interval)
    }
}